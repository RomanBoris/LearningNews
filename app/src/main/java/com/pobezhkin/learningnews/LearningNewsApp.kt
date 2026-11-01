package com.pobezhkin.learningnews

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.pobezhkin.learningnews.domain.usecase.ResumePendingDownloadsUseCase
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltAndroidApp
class LearningNewsApp : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var resumePendingDownloads: ResumePendingDownloadsUseCase

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    // Чтобы фоновые задачи получали зависимости через Hilt, WorkManager создаёт их нашей фабрикой
    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        // Тексты новостей, сохранённых без сети (или до появления этой функции), ставим в очередь
        appScope.launch { resumePendingDownloads() }
    }
}
