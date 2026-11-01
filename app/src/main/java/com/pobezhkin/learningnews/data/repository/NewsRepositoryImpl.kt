package com.pobezhkin.learningnews.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.pobezhkin.learningnews.data.local.NewsDatabase
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
import com.pobezhkin.learningnews.data.local.dao.RemoteKeyDao
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.paging.NewsRemoteMediator
import com.pobezhkin.learningnews.data.remote.NewsApiService
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val database: NewsDatabase,
    private val newsCacheDao: NewsCacheDao,
    private val remoteKeyDao: RemoteKeyDao
) : NewsRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getNewsPaged(category: NewsCategory): Flow<PagingData<News>> =
        Pager(
            config = PagingConfig(
                pageSize = NewsRemoteMediator.PAGE_SIZE,
                // первая порция — одна страница, иначе подгрузятся сразу три
                initialLoadSize = NewsRemoteMediator.PAGE_SIZE,
                // следующую страницу просим, когда до конца списка осталось 5 новостей
                prefetchDistance = PREFETCH_DISTANCE,
                enablePlaceholders = false
            ),
            remoteMediator = NewsRemoteMediator(
                category = category,
                newsApiService = newsApiService,
                database = database,
                newsCacheDao = newsCacheDao,
                remoteKeyDao = remoteKeyDao
            ),
            pagingSourceFactory = { newsCacheDao.pagingSource(category.name) }
        ).flow.map { pagingData -> pagingData.map { it.toNews() } }

    private companion object {
        const val PREFETCH_DISTANCE = 5
    }
}
