package com.pobezhkin.learningnews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity

@Dao
abstract class NewsCacheDao {

    @Query("SELECT * FROM cached_news WHERE category = :category ORDER BY position ASC")
    abstract suspend fun getByCategory(category: String): List<CachedNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    protected abstract suspend fun insertAll(news: List<CachedNewsEntity>)

    @Query("DELETE FROM cached_news WHERE category = :category")
    protected abstract suspend fun deleteByCategory(category: String)

    /** Меняет кэш одной темы на свежую ленту одной транзакцией: либо всё, либо ничего. */
    @Transaction
    open suspend fun replaceCategory(category: String, news: List<CachedNewsEntity>) {
        deleteByCategory(category)
        insertAll(news)
    }
}
