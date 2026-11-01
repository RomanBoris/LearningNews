package com.pobezhkin.learningnews.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.pobezhkin.learningnews.presentation.navigation.canNavigateBackFrom
import com.pobezhkin.learningnews.presentation.navigation.titleResFor
import com.pobezhkin.learningnews.presentation.navigation.topLevelRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NewsApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(stringResource(titleResFor(currentRoute))) },
                navigationIcon = {
                    if (canNavigateBackFrom(currentRoute)) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = stringResource(R.string.action_back)
                            )
                        }
                    }
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
