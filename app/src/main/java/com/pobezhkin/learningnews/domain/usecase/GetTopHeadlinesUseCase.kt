package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import javax.inject.Inject

class GetTopHeadlinesUseCase @Inject constructor(
   private val newsRepository: NewsRepository
) {
        suspend operator fun invoke(): List<News>{

                 return   newsRepository.getTopHeadlines()
        }
}