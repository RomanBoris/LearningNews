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

    private fun loadNews() {
        viewModelScope.launch {
            _state.value = when (val result = getTopHeadlinesUseCase()) {
                is Result.Success -> HomeUiState.Success(result.data)
                is Result.Error -> HomeUiState.Error(result.message)
            }
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
