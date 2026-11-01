package com.pobezhkin.learningnews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import com.pobezhkin.learningnews.domain.usecase.ObserveWatchLaterUseCase
import com.pobezhkin.learningnews.domain.usecase.ToggleWatchLaterUseCase
import com.pobezhkin.learningnews.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    observeWatchLaterUseCase: ObserveWatchLaterUseCase,
    private val toggleWatchLaterUseCase: ToggleWatchLaterUseCase
) : ViewModel() {

    /** Темы в порядке вкладок. */
    val categories: List<NewsCategory> = NewsCategory.entries

    /** Ленты тех тем, которые уже открывали. Темы, которых здесь нет, ещё не загружались. */
    private val _feeds = MutableStateFlow<Map<NewsCategory, FeedUiState>>(emptyMap())
    val feeds: StateFlow<Map<NewsCategory, FeedUiState>> = _feeds.asStateFlow()

    /** Темы, которые обновляются свайпом: список остаётся на экране, сверху крутится индикатор. */
    private val _refreshing = MutableStateFlow<Set<NewsCategory>>(emptySet())
    val refreshing: StateFlow<Set<NewsCategory>> = _refreshing.asStateFlow()

    /** Адреса новостей из «Смотреть позже» — по ним карточка рисует заполненное сердечко. */
    val savedUrls: StateFlow<Set<String>> = observeWatchLaterUseCase()
        .map { savedNews -> savedNews.map { it.url }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptySet()
        )

    /**
     * Вкладку показали на экране. Грузим её только в первый раз: так мы не тратим
     * запросы на темы, до которых пользователь не дошёл (на бесплатном ключе их 100 в день).
     */
    fun onCategoryShown(category: NewsCategory) {
        if (_feeds.value.containsKey(category)) return
        load(category)
    }

    /** Кнопка «Обновить» на экране ошибки: возвращаем крутилку и грузим заново. */
    fun onRetryClick(category: NewsCategory) {
        load(category)
    }

    /** Свайп вниз или кнопка «Обновить» в плашке офлайн-режима. */
    fun onRefresh(category: NewsCategory) {
        if (category in _refreshing.value) return
        viewModelScope.launch {
            _refreshing.update { it + category }
            try {
                setFeed(category, loadFeed(category))
            } finally {
                _refreshing.update { it - category }
            }
        }
    }

    fun onWatchLaterClick(news: News) {
        viewModelScope.launch {
            toggleWatchLaterUseCase(news)
        }
    }

    private fun load(category: NewsCategory) {
        setFeed(category, FeedUiState.Loading)
        viewModelScope.launch {
            setFeed(category, loadFeed(category))
        }
    }

    private fun setFeed(category: NewsCategory, feed: FeedUiState) {
        _feeds.update { it + (category to feed) }
    }

    private suspend fun loadFeed(category: NewsCategory): FeedUiState =
        when (val result = getTopHeadlinesUseCase(category)) {
            is Result.Success -> FeedUiState.Success(
                news = result.data.news,
                isOffline = result.data.isFromCache
            )
            is Result.Error -> FeedUiState.Error(result.message)
        }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
