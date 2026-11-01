package com.pobezhkin.learningnews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.News
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
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
    observeWatchLaterUseCase: ObserveWatchLaterUseCase,
    private val toggleWatchLaterUseCase: ToggleWatchLaterUseCase
) : ViewModel() {

    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    /** Идёт обновление свайпом: список остаётся на экране, сверху крутится индикатор. */
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    /** Адреса новостей из «Смотреть позже» — по ним карточка рисует заполненное сердечко. */
    val savedUrls: StateFlow<Set<String>> = observeWatchLaterUseCase()
        .map { savedNews -> savedNews.map { it.url }.toSet() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = emptySet()
        )

    init {
        loadNews()
    }

    fun onWatchLaterClick(news: News) {
        viewModelScope.launch {
            toggleWatchLaterUseCase(news)
        }
    }

    /** Кнопка «Обновить» на экране ошибки: возвращаем крутилку и грузим заново. */
    fun onRetryClick() {
        _state.value = HomeUiState.Loading
        loadNews()
    }

    /** Свайп вниз или кнопка «Обновить» в плашке офлайн-режима. */
    fun onRefresh() {
        if (_isRefreshing.value) return
        viewModelScope.launch {
            _isRefreshing.value = true
            try {
                _state.value = loadState()
            } finally {
                _isRefreshing.value = false
            }
        }
    }

    private fun loadNews() {
        viewModelScope.launch {
            _state.value = loadState()
        }
    }

    private suspend fun loadState(): HomeUiState =
        when (val result = getTopHeadlinesUseCase()) {
            is Result.Success -> HomeUiState.Success(
                news = result.data.news,
                isOffline = result.data.isFromCache
            )
            is Result.Error -> HomeUiState.Error(result.message)
        }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
