package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.data.local.entity.WatchLaterNewsEntity
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.SavedNews

/** Только что сохранённая новость: текст ещё не загружен, поэтому статус PENDING. */
fun News.toWatchLaterEntity(savedAt: Long) = WatchLaterNewsEntity(
    url = url,
    author = author,
    title = title,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    sourceName = sourceName,
    savedAt = savedAt,
    content = null,
    contentStatus = ArticleTextStatus.PENDING.name
)

fun WatchLaterNewsEntity.toNews() = News(
    id = url,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    sourceName = sourceName
)

fun WatchLaterNewsEntity.toSavedNews() = SavedNews(
    news = toNews(),
    textStatus = contentStatus.toTextStatus(),
    text = content
)

private fun String.toTextStatus(): ArticleTextStatus =
    ArticleTextStatus.entries.firstOrNull { it.name == this } ?: ArticleTextStatus.PENDING
