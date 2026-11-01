package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.presentation.components.NewsCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.state.collectAsState()
    val savedUrls by viewModel.savedUrls.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    when (val state = uiState) {
        is HomeUiState.Loading -> LoadingContent(modifier)

        is HomeUiState.Error -> ErrorContent(
            message = state.message,
            onRetryClick = viewModel::onRetryClick,
            modifier = modifier
        )

        is HomeUiState.Success -> PullToRefreshBox(
            isRefreshing = isRefreshing,
            onRefresh = viewModel::onRefresh,
            modifier = modifier
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (state.isOffline) {
                    item {
                        OfflineBanner(onRefreshClick = viewModel::onRefresh)
                    }
                }

                if (state.news.isEmpty()) {
                    item {
                        EmptyContent(modifier = Modifier.fillParentMaxSize())
                    }
                }

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
private fun ErrorContent(
    message: String,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = message, textAlign = TextAlign.Center)
        Button(
            onClick = onRetryClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.action_refresh))
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = stringResource(R.string.home_empty),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Показываем, когда лента взята из кэша: сервер недоступен, но старые новости есть. */
@Composable
private fun OfflineBanner(
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.secondaryContainer
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.home_offline_banner),
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            TextButton(onClick = onRefreshClick) {
                Text(stringResource(R.string.action_refresh))
            }
        }
    }
}
