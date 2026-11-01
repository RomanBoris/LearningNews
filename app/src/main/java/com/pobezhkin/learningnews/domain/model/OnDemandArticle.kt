package com.pobezhkin.learningnews.domain.model

/** Статья, которую открыли для чтения прямо из ленты, без сохранения в избранное. */
sealed interface OnDemandArticle {

    /** Такой новости нет в кэше ленты. */
    data object NotFound : OnDemandArticle

    /** Текст получен, читать можно. */
    data class Ready(val news: News, val text: String) : OnDemandArticle

    /** Источник не отдаёт текст: показываем только то, что есть в самой новости. */
    data class Unavailable(val news: News) : OnDemandArticle

    /** Нет сети: показываем краткое описание и предлагаем повторить. */
    data class Offline(val news: News) : OnDemandArticle
}
