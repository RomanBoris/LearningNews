package com.pobezhkin.learningnews.presentation.later

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.presentation.components.NewsCard
import com.pobezhkin.learningnews.presentation.later.components.ArticleActions

@Composable
fun WatchLaterScreen(
    onNewsClick: (String) -> Unit,
    onReadClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: WatchLaterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        is WatchLaterUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

        is WatchLaterUiState.Empty -> EmptyContent(modifier)

        is WatchLaterUiState.Content -> LazyColumn(modifier = modifier) {
            items(state.items, key = { it.news.url }) { saved ->
                NewsCard(
                    news = saved.news,
                    isSaved = true,
                    onWatchLaterClick = { viewModel.onRemoveClick(saved.news) },
                    onClick = onNewsClick,
                    extraContent = {
                        ArticleActions(
                            saved = saved,
                            onReadClick = { onReadClick(saved.news.url) },
                            onOriginalClick = { onNewsClick(saved.news.url) }
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun EmptyContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Default.FavoriteBorder,
            contentDescription = null,
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = stringResource(R.string.watch_later_empty_title),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            text = stringResource(R.string.watch_later_empty_hint),
            modifier = Modifier.padding(top = 8.dp),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
