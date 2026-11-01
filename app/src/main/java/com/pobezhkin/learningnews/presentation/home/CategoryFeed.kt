package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.presentation.components.NewsCard
import com.pobezhkin.learningnews.presentation.home.components.FeedCardActions

/**
 * Лента одной темы. Что показывать, решаем по состоянию загрузки:
 * пусто и грузим — крутилка, пусто и ошибка — экран ошибки, есть новости — список.
 * Если новости есть, но обновить не вышло, над списком появляется плашка офлайн-режима.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryFeed(
    items: LazyPagingItems<News>,
    savedUrls: Set<String>,
    onNewsClick: (String) -> Unit,
    onReadClick: (String) -> Unit,
    onWatchLaterClick: (News) -> Unit,
    modifier: Modifier = Modifier
) {
    val refreshState = items.loadState.refresh
    val appendState = items.loadState.append
    val isEmpty = items.itemCount == 0

    when {
        isEmpty && refreshState is LoadState.Loading -> LoadingContent(modifier)

        isEmpty && refreshState is LoadState.Error -> ErrorContent(
            message = refreshState.error.message ?: stringResource(R.string.error_unknown),
            onRetryClick = items::retry,
            modifier = modifier
        )

        else -> PullToRefreshBox(
            isRefreshing = refreshState is LoadState.Loading,
            onRefresh = items::refresh,
            modifier = modifier
        ) {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                if (refreshState is LoadState.Error) {
                    item {
                        OfflineBanner(onRefreshClick = items::refresh)
                    }
                }

                if (isEmpty) {
                    item {
                        EmptyContent(modifier = Modifier.fillParentMaxSize())
                    }
                }

                items(
                    count = items.itemCount,
                    key = items.itemKey { it.url }
                ) { index ->
                    items[index]?.let { news ->
                        NewsCard(
                            news = news,
                            isSaved = news.url in savedUrls,
                            onWatchLaterClick = { onWatchLaterClick(news) },
                            onClick = onNewsClick,
                            extraContent = {
                                FeedCardActions(
                                    onReadClick = { onReadClick(news.url) },
                                    onOriginalClick = { onNewsClick(news.url) }
                                )
                            }
                        )
                    }
                }

                // подвал списка: идёт подгрузка следующей страницы или она не удалась
                when (appendState) {
                    is LoadState.Loading -> item { AppendLoading() }
                    is LoadState.Error -> item { AppendError(onRetryClick = items::retry) }
                    is LoadState.NotLoading -> Unit
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

/** Показываем, когда обновить ленту не вышло, но старые новости есть. */
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

@Composable
private fun AppendLoading(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun AppendError(
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.home_append_error),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        TextButton(onClick = onRetryClick) {
            Text(stringResource(R.string.action_retry))
        }
    }
}
