package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.ArticleFetchResult
import com.pobezhkin.learningnews.domain.model.OnDemandArticle
import com.pobezhkin.learningnews.domain.repository.ArticleTextRepository
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import javax.inject.Inject

/**
 * Готовит к чтению статью из ленты: берёт саму новость из кэша ленты и скачивает текст страницы.
 * В избранное ничего не сохраняется. Если текст получить нельзя, всё равно возвращает новость,
 * чтобы экран мог показать краткое описание и объяснить, почему так.
 */
class LoadArticleForReadingUseCase @Inject constructor(
    private val newsRepository: NewsRepository,
    private val articleTextRepository: ArticleTextRepository
) {
    suspend operator fun invoke(url: String): OnDemandArticle {
        val news = newsRepository.getCachedNews(url) ?: return OnDemandArticle.NotFound

        return when (val result = articleTextRepository.fetchText(url)) {
            is ArticleFetchResult.Text -> OnDemandArticle.Ready(news, result.text)
            ArticleFetchResult.Unavailable -> OnDemandArticle.Unavailable(news)
            ArticleFetchResult.NetworkError -> OnDemandArticle.Offline(news)
        }
    }
}
