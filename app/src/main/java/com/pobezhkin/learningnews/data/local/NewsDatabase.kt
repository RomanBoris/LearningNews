package com.pobezhkin.learningnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pobezhkin.learningnews.data.local.dao.NewsCacheDao
import com.pobezhkin.learningnews.data.local.dao.WatchLaterDao
import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity
import com.pobezhkin.learningnews.data.local.entity.WatchLaterNewsEntity

@Database(
    entities = [
        WatchLaterNewsEntity::class,
        CachedNewsEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun watchLaterDao(): WatchLaterDao
    abstract fun newsCacheDao(): NewsCacheDao
}
