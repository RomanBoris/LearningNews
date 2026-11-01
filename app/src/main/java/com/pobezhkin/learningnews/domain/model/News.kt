package com.pobezhkin.learningnews.domain.model

data class News(
    val id: String,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String
) {
    /** Автор для показа. Иногда вместо имени приходит ссылка на сайт: такое не показываем. */
    val authorName: String?
        get() = author?.takeIf { it.isNotBlank() && !it.startsWith("http") }
}
