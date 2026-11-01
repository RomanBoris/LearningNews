package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.pobezhkin.learningnews.presentation.home.components.CategoryTabRow
import kotlinx.coroutines.launch
import kotlin.math.abs

/** Главная: вкладки с темами сверху, под ними лента выбранной темы. Темы листаются свайпом. */
@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val savedUrls by viewModel.savedUrls.collectAsState()

    val categories = viewModel.categories
    val pagerState = rememberPagerState(pageCount = { categories.size })
    val scope = rememberCoroutineScope()

    Column(modifier = modifier) {
        CategoryTabRow(
            categories = categories,
            selectedIndex = pagerState.currentPage,
            onCategoryClick = { index ->
                scope.launch {
                    // К далёкой вкладке прыгаем без анимации: по пути темы «мелькнут»
                    // и загрузятся впустую, а запросов в день ограниченное число.
                    if (abs(index - pagerState.currentPage) > 1) {
                        pagerState.scrollToPage(index)
                    } else {
                        pagerState.animateScrollToPage(index)
                    }
                }
            }
        )

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize()
        ) { page ->
            // Поток страниц темы создаётся, когда страница впервые появилась на экране
            val items = viewModel.newsFlow(categories[page]).collectAsLazyPagingItems()

            CategoryFeed(
                items = items,
                savedUrls = savedUrls,
                onNewsClick = onNewsClick,
                onWatchLaterClick = viewModel::onWatchLaterClick
            )
        }
    }
}
