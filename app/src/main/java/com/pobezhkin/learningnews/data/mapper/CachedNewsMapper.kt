package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity
import com.pobezhkin.learningnews.domain.model.News

fun News.toCachedEntity(position: Int) = CachedNewsEntity(
    url = url,
    author = author,
    title = title,
    description = description,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    sourceName = sourceName,
    position = position
)

fun CachedNewsEntity.toNews() = News(
    id = url,
    author = author,
    title = title,
    description = description,
    url = url,
    urlToImage = urlToImage,
    publishedAt = publishedAt,
    sourceName = sourceName
)
