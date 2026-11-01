package com.pobezhkin.learningnews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pobezhkin.learningnews.data.local.entity.WatchLaterNewsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchLaterDao {

    @Query("SELECT * FROM watch_later_news ORDER BY savedAt DESC")
    fun observeAll(): Flow<List<WatchLaterNewsEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watch_later_news WHERE url = :url)")
    suspend fun exists(url: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: WatchLaterNewsEntity)

    @Query("DELETE FROM watch_later_news WHERE url = :url")
    suspend fun deleteByUrl(url: String)
}
