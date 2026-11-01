package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome
import com.pobezhkin.learningnews.domain.repository.ArticleTextRepository
import javax.inject.Inject

class DownloadArticleTextUseCase @Inject constructor(
    private val repository: ArticleTextRepository
) {
    suspend operator fun invoke(url: String): ArticleDownloadOutcome = repository.downloadText(url)
}
