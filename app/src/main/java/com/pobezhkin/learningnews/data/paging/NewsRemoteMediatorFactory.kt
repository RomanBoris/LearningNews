package com.pobezhkin.learningnews.data.paging

import com.pobezhkin.learningnews.BuildConfig
import com.pobezhkin.learningnews.data.local.NewsDatabase
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
import com.pobezhkin.learningnews.data.local.dao.RemoteKeyDao
import com.pobezhkin.learningnews.data.remote.NewsApiService
import com.pobezhkin.learningnews.domain.model.NewsCategory
import javax.inject.Inject

/**
 * Собирает [NewsRemoteMediator] для темы. Ключ News API и страна задаются здесь, а не внутри
 * медиатора: так его можно проверить в тесте с любыми значениями.
 */
class NewsRemoteMediatorFactory @Inject constructor(
    private val newsApiService: NewsApiService,
    private val database: NewsDatabase,
    private val newsCacheDao: NewsCacheDao,
    private val remoteKeyDao: RemoteKeyDao
) {

    fun create(category: NewsCategory) = NewsRemoteMediator(
        category = category,
        newsApiService = newsApiService,
        database = database,
        newsCacheDao = newsCacheDao,
        remoteKeyDao = remoteKeyDao,
        apiKey = BuildConfig.NEWS_API_KEY,
        country = COUNTRY
    )

    private companion object {
        const val COUNTRY = "us"
    }
}
