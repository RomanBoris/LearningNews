package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.ArticleBlock
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.SavedNews
import kotlinx.coroutines.flow.Flow

interface WatchLaterRepository {

    /** Все сохранённые новости, свежие сверху. Обновляется сама при любом изменении. */
    fun observeSavedNews(): Flow<List<News>>

    /** То же, но вместе с сохранённым текстом и его статусом (для экрана «Смотреть позже»). */
    fun observeSavedArticles(): Flow<List<SavedNews>>

    /** Одна сохранённая новость для экрана чтения. null — её нет в избранном. */
    fun observeSavedArticle(url: String): Flow<SavedNews?>

    suspend fun isSaved(url: String): Boolean

    /** Сохраняет новость без текста: статус PENDING, текст подтянется отдельно. */
    suspend fun save(news: News)

    /**
     * Записывает итог загрузки текста у сохранённой новости.
     * Если новость успели убрать из избранного, ничего не меняется.
     */
    suspend fun updateArticleText(url: String, status: ArticleTextStatus, text: List<ArticleBlock>?)

    suspend fun remove(url: String)

    /** Адреса сохранённых новостей, текст которых ещё не загружен. */
    suspend fun getPendingUrls(): List<String>
}
