package com.pobezhkin.learningnews.di

import com.pobezhkin.learningnews.data.network.AndroidNetworkMonitor
import com.pobezhkin.learningnews.data.repository.ArticleTextRepositoryImpl
import com.pobezhkin.learningnews.data.repository.NewsRepositoryImpl
import com.pobezhkin.learningnews.data.repository.WatchLaterRepositoryImpl
import com.pobezhkin.learningnews.data.util.SystemTimeProvider
import com.pobezhkin.learningnews.data.worker.WorkManagerArticleDownloadScheduler
import com.pobezhkin.learningnews.domain.repository.ArticleTextRepository
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
import com.pobezhkin.learningnews.domain.util.ArticleDownloadScheduler
import com.pobezhkin.learningnews.domain.util.NetworkMonitor
import com.pobezhkin.learningnews.domain.util.TimeProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindNewsRepository(impl: NewsRepositoryImpl): NewsRepository

    @Binds
    abstract fun bindWatchLaterRepository(impl: WatchLaterRepositoryImpl): WatchLaterRepository

    @Binds
    abstract fun bindArticleTextRepository(impl: ArticleTextRepositoryImpl): ArticleTextRepository

    @Binds
    abstract fun bindNetworkMonitor(impl: AndroidNetworkMonitor): NetworkMonitor

    @Binds
    abstract fun bindArticleDownloadScheduler(
        impl: WorkManagerArticleDownloadScheduler
    ): ArticleDownloadScheduler

    @Binds
    abstract fun bindTimeProvider(impl: SystemTimeProvider): TimeProvider
}
