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
import com.pobezhkin.learningnews.presentation.later.WatchNewsLater

@Composable
fun NewsNavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController,
        startDestination = RouteNewsNavigation.NewsList.routeNews
    ) {
        composable(RouteNewsNavigation.NewsList.routeNews) {
            HomeScreen(
                modifier = Modifier,

                onNewsClick = { url ->
                    navController.navigate(
                        RouteNewsNavigation.DetailNews.createRouteNews(Uri.encode(url))
                    )
                }
            )
        }

            composable(
                route = RouteNewsNavigation.DetailNews.routeNews,
                arguments = listOf(
                    navArgument("newsUrl") {
                        type = NavType.StringType
                    }
                )
            ) {backStack ->
                val encoded = backStack.arguments?.getString("newsUrl") ?: ""
                val newsUrl = Uri.decode(encoded)
                DetailNewsScreen(
                    newsUrl = newsUrl
                )
            }

        composable(RouteNewsNavigation.WatchLaterlNews.routeNews) {
            WatchNewsLater()
        }


        }
    }
