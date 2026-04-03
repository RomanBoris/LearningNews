package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pobezhkin.learningnews.presentation.home.screen.NewsCard

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),

    ) {
     val state by viewModel.state.collectAsState()
    val _state = state

    when (_state) {
        is HomeUiState.Error -> {

            Text("${_state.message}")

        }

        HomeUiState.Loading -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(modifier = modifier) }
        }

        is HomeUiState.Success -> {

            LazyColumn(modifier = modifier) {
                items(_state.news) { newsList ->
                    NewsCard(news = newsList)
                }
            }

        }
    }
}

