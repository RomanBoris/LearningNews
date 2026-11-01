package com.pobezhkin.learningnews.data.mapper

import com.pobezhkin.learningnews.domain.model.NewsCategory

/** Значение параметра `category` в запросе к News API. */
fun NewsCategory.toApiValue(): String = when (this) {
    NewsCategory.GENERAL -> "general"
    NewsCategory.TECHNOLOGY -> "technology"
    NewsCategory.BUSINESS -> "business"
    NewsCategory.SCIENCE -> "science"
    NewsCategory.SPORTS -> "sports"
    NewsCategory.HEALTH -> "health"
    NewsCategory.ENTERTAINMENT -> "entertainment"
}
