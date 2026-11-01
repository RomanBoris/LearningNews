package com.pobezhkin.learningnews.data.local.entity

import androidx.room.Entity

/**
 * Последняя загруженная с сервера лента одной темы. Нужна, чтобы главная работала без интернета.
 * Одна и та же новость может попасть в разные темы, поэтому ключ составной: тема + адрес.
 */
@Entity(tableName = "cached_news", primaryKeys = ["category", "url"])
data class CachedNewsEntity(
    /** Имя значения [com.pobezhkin.learningnews.domain.model.NewsCategory]. */
    val category: String,
    val url: String,
    val author: String?,
    val title: String,
    val description: String?,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String,
    /** Место в ленте: сервер присылает новости в нужном порядке, его нельзя потерять. */
    val position: Int
)
