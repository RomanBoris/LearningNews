package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.BuildConfig.NEWS_API_KEY
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.remote.NewApiService
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.util.Result


import com.pobezhkin.learningnews.domain.repository.NewsRepository
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject
import kotlin.collections.emptyList

class NewsRepositoryImpl @Inject constructor(
    private val newApiService: NewApiService
): NewsRepository  {
    override suspend fun getTopHeadlines():  Result<List<News>> {
        return try {
         val response =  newApiService.getTopHeadlines(
                sources = "techcrunch",   NEWS_API_KEY
            ).articles?.map { it.toNews() } ?: emptyList()
            Result.Success(response)
        }catch (e: IOException){
            Result.Error("Нет подключения к интернету")
        } catch (e: HttpException){
            Result.Error("Ошибка сервера: ${e.code()}")
        }catch (e: Exception){
            Result.Error("Неизвестная ошибка")
        }



    }
}