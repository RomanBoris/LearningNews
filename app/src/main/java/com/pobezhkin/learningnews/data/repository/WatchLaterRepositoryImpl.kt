package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.data.local.dao.WatchLaterDao
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.mapper.toSavedNews
import com.pobezhkin.learningnews.data.mapper.toWatchLaterEntity
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchLaterRepositoryImpl @Inject constructor(
    private val watchLaterDao: WatchLaterDao
) : WatchLaterRepository {

    override fun observeSavedNews(): Flow<List<News>> =
        watchLaterDao.observeAll().map { entities -> entities.map { it.toNews() } }

    override fun observeSavedArticles(): Flow<List<SavedNews>> =
        watchLaterDao.observeAll().map { entities -> entities.map { it.toSavedNews() } }

    override fun observeSavedArticle(url: String): Flow<SavedNews?> =
        watchLaterDao.observeByUrl(url).map { entity -> entity?.toSavedNews() }

    override suspend fun isSaved(url: String): Boolean = watchLaterDao.exists(url)

    override suspend fun save(news: News) {
        watchLaterDao.insert(news.toWatchLaterEntity(savedAt = System.currentTimeMillis()))
    }

    override suspend fun remove(url: String) {
        watchLaterDao.deleteByUrl(url)
    }

    override suspend fun getPendingUrls(): List<String> = watchLaterDao.getPendingUrls()
}
