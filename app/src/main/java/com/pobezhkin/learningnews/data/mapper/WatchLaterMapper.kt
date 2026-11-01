package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.data.local.entity.WatchLaterNewsEntity
import com.pobezhkin.learningnews.domain.model.News

fun News.toWatchLaterEntity(savedAt: Long) = WatchLaterNewsEntity(
    url = url,
    author = author,
    title = title,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    sourceName = sourceName,
    savedAt = savedAt
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
