package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import javax.inject.Inject
import com.pobezhkin.learningnews.domain.util.Result

class GetTopHeadlinesUseCase @Inject constructor(
   private val newsRepository: NewsRepository
) {
        suspend operator fun invoke(): Result<List<News>>{

                 return   newsRepository.getTopHeadlines()
        }
}