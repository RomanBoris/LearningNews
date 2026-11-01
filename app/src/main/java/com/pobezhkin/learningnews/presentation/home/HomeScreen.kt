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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pobezhkin.learningnews.presentation.components.NewsCard

@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val savedUrls by viewModel.savedUrls.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> LoadingContent(modifier)
        is HomeUiState.Error -> ErrorContent(message = state.message, modifier = modifier)
        is HomeUiState.Success -> LazyColumn(modifier = modifier) {
            items(state.news) { news ->
                NewsCard(
                    news = news,
                    isSaved = news.url in savedUrls,
                    onWatchLaterClick = { viewModel.onWatchLaterClick(news) },
                    onClick = onNewsClick
                )
            }
        }
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorContent(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = message)
    }
}
