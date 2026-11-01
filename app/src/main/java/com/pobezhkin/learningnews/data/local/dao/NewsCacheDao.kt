package com.pobezhkin.learningnews.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity

@Dao
interface NewsCacheDao {

    /** Источник страниц для списка. Room сам обновляет его, когда в таблицу попадают новые новости. */
    @Query("SELECT * FROM cached_news WHERE category = :category ORDER BY position ASC")
    fun pagingSource(category: String): PagingSource<Int, CachedNewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(news: List<CachedNewsEntity>)

    @Query("DELETE FROM cached_news WHERE category = :category")
    suspend fun deleteByCategory(category: String)
}
