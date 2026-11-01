package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.BuildConfig.NEWS_API_KEY
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.remote.NewsApiService
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import com.pobezhkin.learningnews.domain.util.Result
import kotlinx.coroutines.CancellationException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newsApiService: NewsApiService
) : NewsRepository {

    override suspend fun getTopHeadlines(): Result<List<News>> {
        return try {
            val news = newsApiService.getTopHeadlines(
                sources = SOURCE,
                apiKey = NEWS_API_KEY
            ).articles?.map { it.toNews() } ?: emptyList()
            Result.Success(news)
        } catch (e: IOException) {
            Result.Error("Нет подключения к интернету")
        } catch (e: HttpException) {
            Result.Error("Ошибка сервера: ${e.code()}")
        } catch (e: CancellationException) {
            // отмену корутины нельзя проглатывать
            throw e
        } catch (e: Exception) {
            Result.Error("Неизвестная ошибка")
        }
    }

    private companion object {
        const val SOURCE = "techcrunch"
    }
}
