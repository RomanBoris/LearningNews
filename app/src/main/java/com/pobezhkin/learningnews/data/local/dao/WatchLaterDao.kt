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

    @Query("SELECT * FROM watch_later_news WHERE url = :url")
    fun observeByUrl(url: String): Flow<WatchLaterNewsEntity?>

    @Query("SELECT EXISTS(SELECT 1 FROM watch_later_news WHERE url = :url)")
    suspend fun exists(url: String): Boolean

    /** Адреса новостей, у которых текст ещё не загружен. */
    @Query("SELECT url FROM watch_later_news WHERE contentStatus = 'PENDING'")
    suspend fun getPendingUrls(): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(news: WatchLaterNewsEntity)

    /** Если новость успели убрать из избранного, ничего не изменится. */
    @Query("UPDATE watch_later_news SET contentStatus = :status, content = :content WHERE url = :url")
    suspend fun updateContent(url: String, status: String, content: String?)

    @Query("DELETE FROM watch_later_news WHERE url = :url")
    suspend fun deleteByUrl(url: String)
}
