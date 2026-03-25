package com.pobezhkin.learningnews.presentation.home

import com.pobezhkin.learningnews.domain.model.News

sealed class HomeUiState {
    object Loading: HomeUiState()
    data class Success(val news : List<News>) : HomeUiState()
    data class Error(val error : String = "ERROR") : HomeUiState()
}