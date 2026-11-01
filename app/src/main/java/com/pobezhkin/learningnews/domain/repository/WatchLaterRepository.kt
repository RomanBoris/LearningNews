package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.News
import kotlinx.coroutines.flow.Flow

interface WatchLaterRepository {

    /** Все сохранённые новости, свежие сверху. Обновляется сама при любом изменении. */
    fun observeSavedNews(): Flow<List<News>>

    suspend fun isSaved(url: String): Boolean

    suspend fun save(news: News)

    suspend fun remove(url: String)
}
