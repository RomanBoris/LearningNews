package com.pobezhkin.learningnews.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pobezhkin.learningnews.presentation.components.NewsTopBar
import com.pobezhkin.learningnews.presentation.navigation.NewsBottomBar
import com.pobezhkin.learningnews.presentation.navigation.NewsNavigationGraph
import com.pobezhkin.learningnews.presentation.navigation.canNavigateBackFrom
import com.pobezhkin.learningnews.presentation.navigation.titleResFor
import com.pobezhkin.learningnews.presentation.navigation.topLevelRoutes

@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            NewsTopBar(
                title = stringResource(titleResFor(currentRoute)),
                onBackClick = if (canNavigateBackFrom(currentRoute)) {
                    { navController.popBackStack() }
                } else {
                    null
                }
            )
        },
        bottomBar = {
            if (currentRoute in topLevelRoutes) NewsBottomBar(navController)
        }
    ) { innerPadding ->
        NewsNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
