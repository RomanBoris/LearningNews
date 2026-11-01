package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.pobezhkin.learningnews.presentation.home.components.CategoryTabRow
import kotlinx.coroutines.launch

/** Главная: вкладки с темами сверху, под ними лента выбранной темы. Темы листаются свайпом. */
@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val feeds by viewModel.feeds.collectAsState()
    val refreshing by viewModel.refreshing.collectAsState()
    val savedUrls by viewModel.savedUrls.collectAsState()

    val categories = viewModel.categories
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        CategoryTabRow(
            categories = categories,
            selectedIndex = pagerState.currentPage,
            onCategoryClick = { index ->
                scope.launch { pagerState.animateScrollToPage(index) }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            val category = categories[page]

            // Страница появилась на экране — просим загрузить её тему (повторно не грузится)
            LaunchedEffect(category) {
                viewModel.onCategoryShown(category)
            }

            CategoryFeed(
                feed = feeds[category] ?: FeedUiState.Loading,
                isRefreshing = category in refreshing,
                savedUrls = savedUrls,
                onNewsClick = onNewsClick,
                onWatchLaterClick = viewModel::onWatchLaterClick,
                onRefresh = { viewModel.onRefresh(category) },
                onRetryClick = { viewModel.onRetryClick(category) }
            )
        }
    }
}
