package com.pobezhkin.learningnews.presentation.reader

import com.pobezhkin.learningnews.domain.model.SavedNews

sealed class ReaderUiState {
    data object Loading : ReaderUiState()

    /** Новости нет в избранном (например, её убрали, пока экран был открыт). */
    data object NotFound : ReaderUiState()

    data class Content(val saved: SavedNews) : ReaderUiState()
}
