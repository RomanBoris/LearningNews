package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome
import com.pobezhkin.learningnews.domain.model.ArticleFetchResult

interface ArticleTextRepository {

    /** Скачивает страницу статьи и достаёт из неё текст. Ничего не сохраняет. */
    suspend fun fetchText(url: String): ArticleFetchResult

    /**
     * То же, но результат сохраняется у новости в избранном.
     * Если новость успели убрать из избранного, сохранять некуда и ничего не меняется.
     */
    suspend fun downloadText(url: String): ArticleDownloadOutcome
}
