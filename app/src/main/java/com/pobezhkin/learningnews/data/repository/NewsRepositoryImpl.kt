package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.BuildConfig.NEWS_API_KEY
import com.pobezhkin.learningnews.data.mapper.toNews
import com.pobezhkin.learningnews.data.remote.NewApiService
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val newApiService: NewApiService
): NewsRepository  {
    override suspend fun getTopHeadlines(): List<News> {
        return newApiService.getTopHeadlines(
            "us", NEWS_API_KEY
        ).articles?.map { it.toNews() } ?: emptyList()
    }
}