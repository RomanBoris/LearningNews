package com.pobezhkin.learningnews.di

import android.content.Context
import androidx.room.Room
import com.pobezhkin.learningnews.data.local.MIGRATION_1_2
import com.pobezhkin.learningnews.data.local.NewsDatabase
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
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
        Room.databaseBuilder(context, NewsDatabase::class.java, DATABASE_NAME)
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideWatchLaterDao(database: NewsDatabase): WatchLaterDao = database.watchLaterDao()

    @Provides
    fun provideNewsCacheDao(database: NewsDatabase): NewsCacheDao = database.newsCacheDao()
}
