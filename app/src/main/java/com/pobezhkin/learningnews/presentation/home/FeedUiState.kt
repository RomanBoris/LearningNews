package com.pobezhkin.learningnews.presentation.home

import com.pobezhkin.learningnews.domain.model.News

/** Состояние ленты одной темы (одной вкладки). */
sealed class FeedUiState {
    data object Loading : FeedUiState()

    /** [isOffline] = true, когда показываем сохранённую ленту, потому что свежую загрузить не вышло. */
    data class Success(
        val news: List<News>,
        val isOffline: Boolean = false
    ) : FeedUiState()

    data class Error(val message: String) : FeedUiState()
}
