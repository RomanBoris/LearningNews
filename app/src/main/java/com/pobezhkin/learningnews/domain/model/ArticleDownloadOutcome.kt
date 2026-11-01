package com.pobezhkin.learningnews.domain.model

/** Чем закончилась попытка загрузить текст статьи. */
enum class ArticleDownloadOutcome {
    /** Текст загружен и сохранён. */
    READY,

    /** Сайт отказал или текста на странице нет: повторять бессмысленно. */
    UNAVAILABLE,

    /** Не вышло из-за сети или временной ошибки сервера: стоит попробовать позже. */
    RETRY_LATER
}
