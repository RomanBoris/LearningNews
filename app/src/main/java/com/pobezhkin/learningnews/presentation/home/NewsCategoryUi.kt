package com.pobezhkin.learningnews.presentation.home

import androidx.annotation.StringRes
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.domain.model.NewsCategory

/** Название темы на вкладке. Домен о строках ничего не знает, поэтому сопоставление здесь. */
@StringRes
fun NewsCategory.titleRes(): Int = when (this) {
    NewsCategory.GENERAL -> R.string.category_general
    NewsCategory.TECHNOLOGY -> R.string.category_technology
    NewsCategory.BUSINESS -> R.string.category_business
    NewsCategory.SCIENCE -> R.string.category_science
    NewsCategory.SPORTS -> R.string.category_sports
    NewsCategory.HEALTH -> R.string.category_health
    NewsCategory.ENTERTAINMENT -> R.string.category_entertainment
}
