package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(): NewsRepository  {
    override suspend fun getTopHeadlines(): List<News> {
        return listOf<News>(News(
            id = "1",
            title = "В эфире новости",
            description = "Она упала в реку",
            url = "https://habr.com/ru/companies/bsl/articles/788940/",
            imageUrl = "https://habr.com/ru/companies/bsl/articles/788940/",
            publishedAt = "03.05",
            sourceName = "BBC",
        ))
    }
}