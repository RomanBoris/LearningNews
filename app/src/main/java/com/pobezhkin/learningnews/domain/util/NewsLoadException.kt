package com.pobezhkin.learningnews.domain.util

/**
 * Ошибка загрузки новостей. [message] уже готов для показа пользователю,
 * поэтому экран не зависит от Retrofit и OkHttp.
 */
class NewsLoadException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause)
