package com.pobezhkin.learningnews.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.pobezhkin.learningnews.BuildConfig.NEWS_API_KEY
import com.pobezhkin.learningnews.data.local.NewsDatabase
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
import com.pobezhkin.learningnews.data.local.dao.RemoteKeyDao
import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity
import com.pobezhkin.learningnews.data.local.entity.RemoteKeyEntity
import com.pobezhkin.learningnews.data.mapper.toApiValue
import com.pobezhkin.learningnews.data.mapper.toCachedEntity
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.remote.NewsApiService
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.util.NewsLoadException
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Приносит страницы с сервера в базу. Список на экране читает только из базы,
 * поэтому всё, что уже загружено, видно и без интернета.
 */
@OptIn(ExperimentalPagingApi::class)
class NewsRemoteMediator(
    private val category: NewsCategory,
    private val newsApiService: NewsApiService,
    private val database: NewsDatabase,
    private val newsCacheDao: NewsCacheDao,
    private val remoteKeyDao: RemoteKeyDao
) : RemoteMediator<Int, CachedNewsEntity>() {

    /** При первом показе темы сначала обновляем её с сервера. Пока грузится, виден кэш. */
    override suspend fun initialize(): InitializeAction = InitializeAction.LAUNCH_INITIAL_REFRESH

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CachedNewsEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> FIRST_PAGE
            // подгружать «вверх» нечего: лента всегда начинается с первой страницы
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> remoteKeyDao.getByCategory(category.name)?.nextPage
                ?: return MediatorResult.Success(endOfPaginationReached = true)
        }

        return try {
            val response = newsApiService.getTopHeadlines(
                country = COUNTRY,
                category = category.toApiValue(),
                page = page,
                pageSize = PAGE_SIZE,
                apiKey = NEWS_API_KEY
            )
            val articles = response.articles.orEmpty()

            val news = articles
                .map { it.toNews() }
                // без адреса новость не открыть и не сохранить
                .filter { it.url.isNotBlank() }
            // Страница может прийти короче 20 и не быть последней: сервер выкидывает часть статей
            // уже после нарезки. Поэтому конец считаем по общему числу, а не по длине страницы.
            val totalResults = response.totalResults
            val endOfPaginationReached =
                articles.isEmpty() || (totalResults != null && page * PAGE_SIZE >= totalResults)

            // Кэш меняем только после успешного ответа: при ошибке старые новости остаются
            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    newsCacheDao.deleteByCategory(category.name)
                    remoteKeyDao.deleteByCategory(category.name)
                }
                val firstPosition = (page - 1) * PAGE_SIZE
                newsCacheDao.insertAll(
                    news.mapIndexed { index, item ->
                        item.toCachedEntity(category, firstPosition + index)
                    }
                )
                remoteKeyDao.insert(
                    RemoteKeyEntity(
                        category = category.name,
                        nextPage = if (endOfPaginationReached) null else page + 1
                    )
                )
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: CancellationException) {
            // отмену корутины нельзя проглатывать
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e.toLoadException())
        }
    }

    private fun Throwable.toLoadException(): NewsLoadException = when (this) {
        is IOException -> NewsLoadException("Нет подключения к интернету", this)
        is HttpException -> NewsLoadException("Ошибка сервера: ${code()}", this)
        else -> NewsLoadException("Неизвестная ошибка", this)
    }

    companion object {
        const val PAGE_SIZE = 20
        private const val FIRST_PAGE = 1
        private const val COUNTRY = "us"
    }
}
