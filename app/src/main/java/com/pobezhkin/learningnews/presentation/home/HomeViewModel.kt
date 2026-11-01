package com.pobezhkin.learningnews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.model.ToggleWatchLaterResult
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import com.pobezhkin.learningnews.domain.usecase.ObserveWatchLaterUseCase
import com.pobezhkin.learningnews.domain.usecase.ToggleWatchLaterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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

    /**
     * Поток страниц для каждой темы. Создаётся при первом обращении, то есть когда вкладку
     * впервые показали: так мы не тратим запросы на темы, до которых пользователь не дошёл
     * (на бесплатном ключе их 100 в день). cachedIn хранит загруженное во ViewModel,
     * поэтому поворот экрана и возврат на вкладку не грузят новости заново.
     */
    private val newsFlows: Map<NewsCategory, Lazy<Flow<PagingData<News>>>> =
        categories.associateWith { category ->
            lazy { getTopHeadlinesUseCase(category).cachedIn(viewModelScope) }
        }

    /** Адреса новостей из «Смотреть позже» — по ним карточка рисует заполненное сердечко. */
    val savedUrls: StateFlow<Set<String>> = observeWatchLaterUseCase()
        .map { savedNews -> savedNews.map { it.url }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptySet()
        )

    private val _events = Channel<HomeEvent>(Channel.BUFFERED)
    val events: Flow<HomeEvent> = _events.receiveAsFlow()

    fun newsFlow(category: NewsCategory): Flow<PagingData<News>> =
        newsFlows.getValue(category).value

    fun onWatchLaterClick(news: News) {
        viewModelScope.launch {
            val result = toggleWatchLaterUseCase(news)
            if (result is ToggleWatchLaterResult.Saved && result.isOffline) {
                _events.send(HomeEvent.SavedWithoutNetwork)
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
