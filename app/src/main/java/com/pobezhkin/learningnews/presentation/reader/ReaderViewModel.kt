package com.pobezhkin.learningnews.presentation.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.usecase.ObserveSavedArticleUseCase
import com.pobezhkin.learningnews.presentation.navigation.ARG_NEWS_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeSavedArticleUseCase: ObserveSavedArticleUseCase
) : ViewModel() {

    private val url: String = savedStateHandle.get<String>(ARG_NEWS_URL).orEmpty()

    /** Подписка на базу: если текст догрузится, пока экран открыт, он появится сам. */
    val uiState: StateFlow<ReaderUiState> = observeSavedArticleUseCase(url)
        .map { saved ->
            if (saved == null) ReaderUiState.NotFound else ReaderUiState.Content(saved)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = ReaderUiState.Loading
        )

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
