package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Избранное вместе с текстами статей. */
class ObserveSavedArticlesUseCase @Inject constructor(
    private val repository: WatchLaterRepository
) {
    operator fun invoke(): Flow<List<SavedNews>> = repository.observeSavedArticles()
}
