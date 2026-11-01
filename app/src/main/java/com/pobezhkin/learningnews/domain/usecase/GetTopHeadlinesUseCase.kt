package com.pobezhkin.learningnews.domain.usecase

import androidx.paging.PagingData
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
    private val newsRepository: NewsRepository
) {
    operator fun invoke(category: NewsCategory): Flow<PagingData<News>> =
        newsRepository.getNewsPaged(category)
}
