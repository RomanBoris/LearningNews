package com.pobezhkin.learningnews.presentation.reader

import com.pobezhkin.learningnews.domain.model.News

/** Можно ли прочитать полный текст и что показывать, если нельзя. */
enum class TextAvailability {
    /** Текст есть, читаем. */
    READY,

    /** Новость в избранном, текст докачается, когда появится сеть. */
    PENDING_NETWORK,

    /** Источник не отдаёт текст: показываем только краткое описание. */
    UNAVAILABLE,

    /** Читаем из ленты, а сети нет: показываем краткое описание и даём повторить. */
    OFFLINE
}

sealed class ReaderUiState {
    data object Loading : ReaderUiState()

    /** Новости нет ни в избранном, ни в кэше ленты. */
    data object NotFound : ReaderUiState()

    /** [text] заполнен только при [TextAvailability.READY]. */
    data class Content(
        val news: News,
        val text: String?,
        val availability: TextAvailability
    ) : ReaderUiState()
}
