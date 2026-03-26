package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.News

interface NewsRepository {
    suspend fun getTopHeadlines() : List<News>
}