package com.pobezhkin.learningnews

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pobezhkin.learningnews.presentation.home.HomeScreen
import com.pobezhkin.learningnews.ui.theme.LearningNewsTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            LearningNewsTheme {
                HomeScreen()
            }
        }
    }
}



//Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//}