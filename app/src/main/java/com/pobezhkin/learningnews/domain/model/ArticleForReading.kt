package com.pobezhkin.learningnews.domain.model

/** Как идёт подготовка статьи к чтению и откуда она взята. */
sealed interface ArticleForReading {

    /** Текст статьи из ленты скачивается. */
    data object Loading : ArticleForReading

    /** Новость в избранном: всё берётся из базы, сеть не нужна. */
    data class FromSaved(val saved: SavedNews) : ArticleForReading

    /** Новости в избранном нет: её открыли из ленты, итог загрузки текста в [article]. */
    data class FromFeed(val article: OnDemandArticle) : ArticleForReading
}
