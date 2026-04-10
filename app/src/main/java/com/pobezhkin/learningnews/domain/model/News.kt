package com.pobezhkin.learningnews.domain.model

data class News(
    val id: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val imageUrl: String?,
    val publishedAt: String,
    val sourceName: String
)
