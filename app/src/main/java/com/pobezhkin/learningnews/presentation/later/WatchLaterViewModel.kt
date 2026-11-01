package com.pobezhkin.learningnews.presentation.later

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.usecase.ObserveSavedArticlesUseCase
import com.pobezhkin.learningnews.domain.usecase.ToggleWatchLaterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WatchLaterViewModel @Inject constructor(
    observeSavedArticlesUseCase: ObserveSavedArticlesUseCase,
    private val toggleWatchLaterUseCase: ToggleWatchLaterUseCase
) : ViewModel() {

    val uiState: StateFlow<WatchLaterUiState> = observeSavedArticlesUseCase()
        .map { saved ->
            if (saved.isEmpty()) WatchLaterUiState.Empty else WatchLaterUiState.Content(saved)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = WatchLaterUiState.Loading
        )

    /** Новость на этом экране всегда сохранена, поэтому переключение её убирает. */
    fun onRemoveClick(news: News) {
        viewModelScope.launch {
            toggleWatchLaterUseCase(news)
        }
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
