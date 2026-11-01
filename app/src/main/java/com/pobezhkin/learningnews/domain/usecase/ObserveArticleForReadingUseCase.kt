package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.ArticleForReading
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

/**
 * Готовит статью к чтению. Если новость в избранном, берём её из базы: так текст
 * доступен без сети и обновляется сам, когда докачается. Если нет, статью открыли из ленты,
 * и текст скачивается по нажатию.
 */
class ObserveArticleForReadingUseCase @Inject constructor(
    private val watchLaterRepository: WatchLaterRepository,
    private val loadArticleForReading: LoadArticleForReadingUseCase
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(url: String): Flow<ArticleForReading> =
        watchLaterRepository.observeSavedArticle(url)
            // база сообщает об изменении любой сохранённой новости, а нам важна только эта
            .distinctUntilChanged()
            .flatMapLatest { saved ->
                if (saved != null) {
                    flowOf<ArticleForReading>(ArticleForReading.FromSaved(saved))
                } else {
                    flow {
                        emit(ArticleForReading.Loading)
                        emit(ArticleForReading.FromFeed(loadArticleForReading(url)))
                    }
                }
            }
}
