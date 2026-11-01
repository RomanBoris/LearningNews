package com.pobezhkin.learningnews.data.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.pobezhkin.learningnews.domain.util.ArticleDownloadScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.UUID
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class WorkManagerArticleDownloadScheduler @Inject constructor(
    @ApplicationContext private val context: Context
) : ArticleDownloadScheduler {

    override fun schedule(url: String) {
        val request = OneTimeWorkRequestBuilder<ArticleDownloadWorker>()
            .setInputData(workDataOf(ArticleDownloadWorker.KEY_URL to url))
            // без сети задача просто ждёт: запустится сама, когда интернет появится
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, BACKOFF_SECONDS, TimeUnit.SECONDS)
            .build()

        // KEEP: если задача для этой статьи уже стоит в очереди, второй не создаём
        WorkManager.getInstance(context)
            .enqueueUniqueWork(workName(url), ExistingWorkPolicy.KEEP, request)
    }

    override fun cancel(url: String) {
        WorkManager.getInstance(context).cancelUniqueWork(workName(url))
    }

    private fun workName(url: String): String =
        "article-text-" + UUID.nameUUIDFromBytes(url.toByteArray())

    private companion object {
        const val BACKOFF_SECONDS = 30L
    }
}
