package com.pobezhkin.learningnews.presentation.barbottomnavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.pobezhkin.learningnews.presentation.navigation.RouteNewsNavigation

@Composable
fun NewsBottomBarNavigation(navController: NavController) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == RouteNewsNavigation.NewsList.routeNews,
            onClick = {
                navController.navigate(RouteNewsNavigation.NewsList.routeNews) {
                    popUpTo(RouteNewsNavigation.NewsList.routeNews) { inclusive = false }
                    launchSingleTop = true
                }
            },
icon = {Icon(Icons.Default.Home, contentDescription = null)},
            label = { Text("Главная") }
        )


        NavigationBarItem(
            selected = currentRoute == RouteNewsNavigation.WatchLaterlNews.routeNews,
            onClick = {
                navController.navigate(RouteNewsNavigation.WatchLaterlNews.routeNews) {
                    popUpTo(RouteNewsNavigation.NewsList.routeNews) { inclusive = false }
                    launchSingleTop = true
                }
            },
            icon = {Icon(Icons.Default.Favorite, contentDescription = null)},
            label = { Text("Смотреть позже") }
        )
    }

}