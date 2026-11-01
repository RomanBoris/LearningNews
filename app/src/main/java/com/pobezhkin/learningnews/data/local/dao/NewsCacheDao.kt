package com.pobezhkin.learningnews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity

@Dao
abstract class NewsCacheDao {

    @Query("SELECT * FROM cached_news ORDER BY position ASC")
    abstract suspend fun getAll(): List<CachedNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAll(news: List<CachedNewsEntity>)

    @Query("DELETE FROM cached_news")
    protected abstract suspend fun deleteAll()

    /** Меняет весь кэш на свежую ленту одной транзакцией: либо всё, либо ничего. */
    @Transaction
    open suspend fun replaceAll(news: List<CachedNewsEntity>) {
        deleteAll()
        insertAll(news)
    }
}
