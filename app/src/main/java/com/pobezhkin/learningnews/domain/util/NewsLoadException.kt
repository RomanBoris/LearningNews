package com.pobezhkin.learningnews.domain.util

/**
 * Почему не удалось загрузить новости. Текста для пользователя здесь нет:
 * его выбирает экран по типу ошибки, поэтому слой данных не знает ни про язык, ни про ресурсы,
 * а экран не зависит от Retrofit и OkHttp. [message] нужен только для логов.
 */
sealed class NewsLoadException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    /** Нет сети или оборвалось соединение. */
    class NoConnection(cause: Throwable? = null) : NewsLoadException("No connection", cause)

    /** Сервер ответил ошибкой с кодом [code]. */
    class Server(val code: Int, cause: Throwable? = null) :
        NewsLoadException("Server error: $code", cause)

    class Unknown(cause: Throwable? = null) : NewsLoadException("Unknown error", cause)
}
