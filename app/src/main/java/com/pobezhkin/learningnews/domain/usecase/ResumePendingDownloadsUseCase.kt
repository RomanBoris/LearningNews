package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import com.pobezhkin.learningnews.domain.util.ArticleDownloadScheduler
import javax.inject.Inject

/**
 * Ставит в очередь тексты всех сохранённых новостей, которые ещё не загружены.
 * Запускается при старте приложения: так не теряется ничего, что было сохранено
 * без сети или до появления этой возможности.
 */
class ResumePendingDownloadsUseCase @Inject constructor(
    private val repository: WatchLaterRepository,
    private val scheduler: ArticleDownloadScheduler
) {
    suspend operator fun invoke() {
        repository.getPendingUrls().forEach(scheduler::schedule)
    }
}
