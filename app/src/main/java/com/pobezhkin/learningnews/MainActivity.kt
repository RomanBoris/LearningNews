package com.pobezhkin.learningnews

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pobezhkin.learningnews.data.repository.NewsRepositoryImpl
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import com.pobezhkin.learningnews.presentation.home.HomeScreen
import com.pobezhkin.learningnews.presentation.home.HomeViewModel
import com.pobezhkin.learningnews.ui.theme.LearningNewsTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repo = NewsRepositoryImpl()
        val useCase = GetTopHeadlinesUseCase(repo)

        val viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return HomeViewModel(useCase) as T
            }
        })[HomeViewModel::class.java]
        lifecycleScope.launch {

            val news = useCase()
            Log.d("Test", "NewsCount - ${news.size}")
        }

        enableEdgeToEdge()
        setContent {
            LearningNewsTheme {
                HomeScreen(viewModel )
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LearningNewsTheme {
        Greeting("Android")
    }
}

//Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//
//}