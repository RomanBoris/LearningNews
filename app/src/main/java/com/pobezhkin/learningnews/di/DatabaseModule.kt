package com.pobezhkin.learningnews.di

import android.content.Context
import androidx.room.Room
import com.pobezhkin.learningnews.data.local.NewsDatabase
import com.pobezhkin.learningnews.data.local.dao.WatchLaterDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "news.db"

    @Singleton
    @Provides
    fun provideNewsDatabase(@ApplicationContext context: Context): NewsDatabase =
        Room.databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME).build()

    @Provides
    fun provideWatchLaterDao(database: NewsDatabase): WatchLaterDao = database.watchLaterDao()
}
