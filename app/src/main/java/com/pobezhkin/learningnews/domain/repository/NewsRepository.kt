package com.pobezhkin.learningnews.domain.repository

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.util.Result

interface NewsRepository {
    suspend fun getTopHeadlines() : Result<List<News>>
}