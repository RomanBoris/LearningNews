package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.data.remote.dto.NewsDto
import com.pobezhkin.learningnews.domain.model.News

fun NewsDto.toNews() = News(
    id = url ?: "",
    author = author,
    title = title ?: "Заголовок невозможно загрузить",
    description = description,
    url = url ?: "",
    urlToImage = urlToImage,
    publishedAt = publishedAt ?: "",
    sourceName = source?.name ?: ""

)