package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.ToggleWatchLaterResult
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import com.pobezhkin.learningnews.domain.util.ArticleDownloadScheduler
import com.pobezhkin.learningnews.domain.util.NetworkMonitor
import javax.inject.Inject

/**
 * Сохраняет новость в «Смотреть позже», а если она уже там — убирает.
 * При сохранении ставит в очередь загрузку текста статьи: с сетью она пройдёт сразу,
 * без сети дождётся её появления.
 */
class ToggleWatchLaterUseCase @Inject constructor(
    private val repository: WatchLaterRepository,
    private val scheduler: ArticleDownloadScheduler,
    private val networkMonitor: NetworkMonitor
) {
    suspend operator fun invoke(news: News): ToggleWatchLaterResult {
        return if (repository.isSaved(news.url)) {
            repository.remove(news.url)
            scheduler.cancel(news.url)
            ToggleWatchLaterResult.Removed
        } else {
            repository.save(news)
            scheduler.schedule(news.url)
            ToggleWatchLaterResult.Saved(isOffline = !networkMonitor.isOnline())
        }
    }
}
