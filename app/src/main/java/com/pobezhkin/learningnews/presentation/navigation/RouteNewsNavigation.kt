package com.pobezhkin.learningnews.presentation.navigation

sealed class RouteNewsNavigation(var routeNews: String) {
    object NewsList: RouteNewsNavigation("news_list")
    object  DetailNews: RouteNewsNavigation("news_detail/{newsUrl}"){
        fun createRouteNews(newsUrl: String) = "news_detail/$newsUrl"
    }
    object  WatchLaterlNews: RouteNewsNavigation("watch_later")
}