package com.pobezhkin.learningnews.presentation.home

import com.pobezhkin.learningnews.domain.model.News

sealed class HomeUiState {
    data object Loading : HomeUiState()

    /** [isOffline] = true, когда показываем сохранённую ленту, потому что свежую загрузить не вышло. */
    data class Success(
        val news: List<News>,
        val isOffline: Boolean = false
    ) : HomeUiState()

    data class Error(val message: String) : HomeUiState()
}
