package com.pobezhkin.learningnews.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.pobezhkin.learningnews.data.local.dao.WatchLaterDao
import com.pobezhkin.learningnews.data.local.entity.WatchLaterNewsEntity

@Database(
    entities = [WatchLaterNewsEntity::class],
    version = 1,
    exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract fun watchLaterDao(): WatchLaterDao
}
