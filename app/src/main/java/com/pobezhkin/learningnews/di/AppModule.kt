package com.pobezhkin.learningnews.di

import com.pobezhkin.learningnews.data.repository.NewsRepositoryImpl
import com.pobezhkin.learningnews.data.repository.WatchLaterRepositoryImpl
import com.pobezhkin.learningnews.domain.repository.NewsRepository
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
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
}
