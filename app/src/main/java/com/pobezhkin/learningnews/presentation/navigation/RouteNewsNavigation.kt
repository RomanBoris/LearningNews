package com.pobezhkin.learningnews.presentation.navigation

import android.net.Uri

const val ARG_NEWS_URL = "newsUrl"

sealed class RouteNewsNavigation(val route: String) {

    data object NewsList : RouteNewsNavigation("news_list")

    data object DetailNews : RouteNewsNavigation("news_detail/{$ARG_NEWS_URL}") {
        fun createRoute(newsUrl: String) = "news_detail/${Uri.encode(newsUrl)}"
    }

    data object WatchLater : RouteNewsNavigation("watch_later")
}

/** Экраны с верхней и нижней панелями. Остальные (детали) показываются на весь экран. */
val topLevelRoutes = listOf(
    RouteNewsNavigation.NewsList.route,
    RouteNewsNavigation.WatchLater.route
)
