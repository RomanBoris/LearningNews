package com.pobezhkin.learningnews.presentation.navigation

import android.net.Uri
import androidx.annotation.StringRes
import com.pobezhkin.learningnews.R

const val ARG_NEWS_URL = "newsUrl"

sealed class RouteNewsNavigation(val route: String) {

    data object NewsList : RouteNewsNavigation("news_list")

    /** Оригинал статьи на сайте источника. */
    data object DetailNews : RouteNewsNavigation("news_detail/{$ARG_NEWS_URL}") {
        fun createRoute(newsUrl: String) = "news_detail/${Uri.encode(newsUrl)}"
    }

    /** Сохранённый текст статьи: открывается без сети. */
    data object Reader : RouteNewsNavigation("reader/{$ARG_NEWS_URL}") {
        fun createRoute(newsUrl: String) = "reader/${Uri.encode(newsUrl)}"
    }

    data object WatchLater : RouteNewsNavigation("watch_later")
}

/** Экраны с нижней панелью. Остальные (статья, чтение) показываются на весь экран. */
val topLevelRoutes = listOf(
    RouteNewsNavigation.NewsList.route,
    RouteNewsNavigation.WatchLater.route
)

/** Заголовок верхней панели для экрана с этим маршрутом. */
@StringRes
fun titleResFor(route: String?): Int = when (route) {
    RouteNewsNavigation.WatchLater.route -> R.string.tab_watch_later
    RouteNewsNavigation.DetailNews.route -> R.string.screen_article
    RouteNewsNavigation.Reader.route -> R.string.screen_reader
    else -> R.string.top_bar_title
}

/** Стрелка «назад» нужна на всех экранах, кроме главного. */
fun canNavigateBackFrom(route: String?): Boolean =
    route != null && route != RouteNewsNavigation.NewsList.route
