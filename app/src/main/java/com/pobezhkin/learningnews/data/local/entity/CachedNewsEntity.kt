package com.pobezhkin.learningnews.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Последняя загруженная с сервера лента. Нужна, чтобы главная работала без интернета. */
@Entity(tableName = "cached_news")
data class CachedNewsEntity(
    @PrimaryKey val url: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String,
    /** Место в ленте: сервер присылает новости в нужном порядке, его нельзя потерять. */
    val position: Int
)
