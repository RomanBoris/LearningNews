package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.BuildConfig.NEWS_API_KEY
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
import com.pobezhkin.learningnews.data.mapper.toApiValue
import com.pobezhkin.learningnews.data.mapper.toCachedEntity
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.remote.NewsApiService
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.model.NewsFeed
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import com.pobezhkin.learningnews.domain.util.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService,
    private val newsCacheDao: NewsCacheDao
) : NewsRepository {

    override suspend fun getTopHeadlines(category: NewsCategory): Result<NewsFeed> {
        return try {
            val news = newsApiService.getTopHeadlines(
                country = COUNTRY,
                category = category.toApiValue(),
                apiKey = NEWS_API_KEY
            ).articles.orEmpty()
                .map { it.toNews() }
                // без адреса новость не открыть и не сохранить
                .filter { it.url.isNotBlank() }

            newsCacheDao.replaceCategory(
                category = category.name,
                news = news.mapIndexed { index, item -> item.toCachedEntity(category, index) }
            )
            Result.Success(NewsFeed(news = news, isFromCache = false))
        } catch (e: IOException) {
            loadFromCache(category, errorMessage = "Нет подключения к интернету")
        } catch (e: HttpException) {
            loadFromCache(category, errorMessage = "Ошибка сервера: ${e.code()}")
        } catch (e: CancellationException) {
            // отмену корутины нельзя проглатывать
            throw e
        } catch (e: Exception) {
            loadFromCache(category, errorMessage = "Неизвестная ошибка")
        }
    }

    /** Старая лента лучше пустого экрана с ошибкой. Ошибку показываем, только если кэш темы пуст. */
    private suspend fun loadFromCache(
        category: NewsCategory,
        errorMessage: String
    ): Result<NewsFeed> {
        val cached = newsCacheDao.getByCategory(category.name).map { it.toNews() }
        return if (cached.isEmpty()) {
            Result.Error(errorMessage)
        } else {
            Result.Success(NewsFeed(news = cached, isFromCache = true))
        }
    }

    private companion object {
        const val COUNTRY = "us"
    }
}
