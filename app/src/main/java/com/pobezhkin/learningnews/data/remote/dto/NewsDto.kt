package com.pobezhkin.learningnews.data.remote.dto

data class NewsDto(
    val title : String?,
    val description: String?,
    val url : String?,
    val urlToImage: String?,
    val publishedAt: String?,
    val source: SourceDto?
)