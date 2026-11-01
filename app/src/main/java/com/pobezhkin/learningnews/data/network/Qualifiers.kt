package com.pobezhkin.learningnews.data.network

import javax.inject.Qualifier

/**
 * HTTP-клиент для скачивания страниц статей. Отдельный от клиента News API:
 * у него другие таймауты и нет подробного лога, иначе в Logcat попадал бы весь HTML.
 */
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class ArticleClient
