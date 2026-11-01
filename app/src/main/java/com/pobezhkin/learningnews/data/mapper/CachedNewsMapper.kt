package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.data.local.entity.CachedNewsEntity
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.model.NewsCategory

fun News.toCachedEntity(category: NewsCategory, position: Int) = CachedNewsEntity(
    category = category.name,
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
