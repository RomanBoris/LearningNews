package com.pobezhkin.learningnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pobezhkin.learningnews.presentation.barbottomnavigation.NewsBottomBarNavigation
import com.pobezhkin.learningnews.presentation.home.HomeScreen
import com.pobezhkin.learningnews.presentation.navigation.NewsNavigationGraph
import com.pobezhkin.learningnews.presentation.navigation.RouteNewsNavigation
import com.pobezhkin.learningnews.ui.theme.LearningNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val  navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = navBackStackEntry?.destination?.route
            val showBottomBar = currentRoute == RouteNewsNavigation.NewsList.routeNews ||
                    currentRoute == RouteNewsNavigation.WatchLaterlNews.routeNews
            LearningNewsTheme {
                Scaffold(
                    topBar =  { CenterAlignedTopAppBar( title = {Text("В ЭФИРЕ НОВОСТИ")},) },
                    bottomBar = { if (showBottomBar) NewsBottomBarNavigation(navController) },
                    modifier = Modifier.fillMaxSize(),
                )  {
                        NewsNavigationGraph(
                            modifier = Modifier.padding(it),
                            navController = navController
                        )

                }

            }
        }
    }
}



//Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//}