package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveWatchLaterUseCase @Inject constructor(
    private val repository: WatchLaterRepository
) {
    operator fun invoke(): Flow<List<News>> = repository.observeSavedNews()
}
