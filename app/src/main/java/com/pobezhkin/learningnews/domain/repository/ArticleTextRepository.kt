package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome

interface ArticleTextRepository {

    /**
     * Скачивает страницу статьи, достаёт из неё текст и сохраняет его у новости в избранном.
     * Если новость успели убрать из избранного, сохранять некуда и ничего не меняется.
     */
    suspend fun downloadText(url: String): ArticleDownloadOutcome
}
