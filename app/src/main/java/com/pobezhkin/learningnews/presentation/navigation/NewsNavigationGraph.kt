package com.pobezhkin.learningnews.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.pobezhkin.learningnews.presentation.detail.DetailNewsScreen
import com.pobezhkin.learningnews.presentation.home.HomeScreen
import com.pobezhkin.learningnews.presentation.later.WatchLaterScreen
import com.pobezhkin.learningnews.presentation.reader.ReaderScreen

@Composable
fun NewsNavigationGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val openNewsDetail: (String) -> Unit = { url ->
        navController.navigate(RouteNewsNavigation.DetailNews.createRoute(url))
    }
    val openReader: (String) -> Unit = { url ->
        navController.navigate(RouteNewsNavigation.Reader.createRoute(url))
    }
    val newsUrlArgument = navArgument(ARG_NEWS_URL) { type = NavType.StringType }

    NavHost(
        navController = navController,
        startDestination = RouteNewsNavigation.NewsList.route,
        modifier = modifier
    ) {
        composable(RouteNewsNavigation.NewsList.route) {
            HomeScreen(onNewsClick = openNewsDetail)
        }

        composable(
            route = RouteNewsNavigation.DetailNews.route,
            arguments = listOf(newsUrlArgument)
        ) { backStackEntry ->
            val encodedUrl = backStackEntry.arguments?.getString(ARG_NEWS_URL).orEmpty()
            DetailNewsScreen(newsUrl = Uri.decode(encodedUrl))
        }

        composable(
            route = RouteNewsNavigation.Reader.route,
            arguments = listOf(newsUrlArgument)
        ) {
            ReaderScreen(onOpenOriginal = openNewsDetail)
        }

        composable(RouteNewsNavigation.WatchLater.route) {
            WatchLaterScreen(
                onNewsClick = openNewsDetail,
                onReadClick = openReader
            )
        }
    }
}
