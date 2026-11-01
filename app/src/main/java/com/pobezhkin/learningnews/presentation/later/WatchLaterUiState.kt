package com.pobezhkin.learningnews.presentation.later

import com.pobezhkin.learningnews.domain.model.News

sealed class WatchLaterUiState {
    data object Loading : WatchLaterUiState()
    data object Empty : WatchLaterUiState()
    data class Content(val news: List<News>) : WatchLaterUiState()
}
