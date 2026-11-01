package com.pobezhkin.learningnews.domain.usecase

import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

/** Одна сохранённая новость для экрана чтения. Эмитит null, если её убрали из избранного. */
class ObserveSavedArticleUseCase @Inject constructor(
    private val repository: WatchLaterRepository
) {
    operator fun invoke(url: String): Flow<SavedNews?> = repository.observeSavedArticle(url)
}
