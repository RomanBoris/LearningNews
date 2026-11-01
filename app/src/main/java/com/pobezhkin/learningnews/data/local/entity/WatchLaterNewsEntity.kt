package com.pobezhkin.learningnews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Новость, которую пользователь сохранил в «Смотреть позже». */
@Entity(tableName = "watch_later_news")
data class WatchLaterNewsEntity(
    @PrimaryKey val url: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String,
    /** Когда сохранили (мс). По нему сортируем: свежие сверху. */
    val savedAt: Long,
    /** Текст статьи для чтения без сети. null, пока он не загружен. */
    val content: String? = null,
    /** Имя значения [com.pobezhkin.learningnews.domain.model.ArticleTextStatus]. */
    val contentStatus: String = "PENDING"
)
