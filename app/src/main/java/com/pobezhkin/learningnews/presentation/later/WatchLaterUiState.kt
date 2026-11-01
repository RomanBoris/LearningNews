package com.pobezhkin.learningnews.presentation.later

import com.pobezhkin.learningnews.domain.model.SavedNews

sealed class WatchLaterUiState {
    data object Loading : WatchLaterUiState()
    data object Empty : WatchLaterUiState()
    data class Content(val items: List<SavedNews>) : WatchLaterUiState()
}
