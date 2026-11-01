package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import javax.inject.Inject

/** Сохраняет новость в «Смотреть позже», а если она уже там — убирает. */
class ToggleWatchLaterUseCase @Inject constructor(
    private val repository: WatchLaterRepository
) {
    suspend operator fun invoke(news: News) {
        if (repository.isSaved(news.url)) {
            repository.remove(news.url)
        } else {
            repository.save(news)
        }
    }
}
