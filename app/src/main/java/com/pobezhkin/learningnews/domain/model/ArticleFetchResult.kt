package com.pobezhkin.learningnews.domain.model

/** Итог попытки скачать текст статьи. Ничего не сохраняет: решает тот, кто спрашивал. */
sealed interface ArticleFetchResult {

    /** Текст получен. */
    data class Text(val blocks: List<ArticleBlock>) : ArticleFetchResult

    /** Сайт отказал или на странице нет статьи: повторять бессмысленно. */
    data object Unavailable : ArticleFetchResult

    /** Нет сети или сервер временно недоступен: позже может получиться. */
    data object NetworkError : ArticleFetchResult
}
