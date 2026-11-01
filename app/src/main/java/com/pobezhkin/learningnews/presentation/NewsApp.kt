package com.pobezhkin.learningnews.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.presentation.navigation.NewsBottomBar
import com.pobezhkin.learningnews.presentation.navigation.NewsNavigationGraph
import com.pobezhkin.learningnews.presentation.navigation.topLevelRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isTopLevelScreen = navBackStackEntry?.destination?.route in topLevelRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (isTopLevelScreen) {
                CenterAlignedTopAppBar(title = { Text(stringResource(R.string.top_bar_title)) })
            }
        },
        bottomBar = {
            if (isTopLevelScreen) NewsBottomBar(navController)
        }
    ) { innerPadding ->
        NewsNavigationGraph(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
