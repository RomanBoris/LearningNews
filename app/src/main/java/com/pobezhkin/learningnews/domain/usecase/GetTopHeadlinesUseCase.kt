package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.model.NewsFeed
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import com.pobezhkin.learningnews.domain.util.Result
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    suspend operator fun invoke(
        category: NewsCategory = NewsCategory.GENERAL
    ): Result<NewsFeed> = newsRepository.getTopHeadlines(category)
}
