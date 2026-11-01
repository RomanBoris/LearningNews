package com.pobezhkin.learningnews.data.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome
import com.pobezhkin.learningnews.domain.usecase.DownloadArticleTextUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

/** Фоновая задача: скачать текст одной статьи. Запускается, когда появляется сеть. */
@HiltWorker
class ArticleDownloadWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val downloadArticleText: DownloadArticleTextUseCase
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val url = inputData.getString(KEY_URL) ?: return Result.failure()

        return when (downloadArticleText(url)) {
            ArticleDownloadOutcome.READY,
            ArticleDownloadOutcome.UNAVAILABLE -> Result.success()
            // WorkManager сам повторит позже, с нарастающей паузой
            ArticleDownloadOutcome.RETRY_LATER -> Result.retry()
        }
    }

    companion object {
        const val KEY_URL = "url"
    }
}
