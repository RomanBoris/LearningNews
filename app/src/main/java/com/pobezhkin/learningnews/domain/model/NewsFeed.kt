package com.pobezhkin.learningnews.domain.model

/** Лента новостей и откуда она взялась: свежая с сервера или сохранённая в кэше. */
data class NewsFeed(
    val news: List<News>,
    val isFromCache: Boolean
)
