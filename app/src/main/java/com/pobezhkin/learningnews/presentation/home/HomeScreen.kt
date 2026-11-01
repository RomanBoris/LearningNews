package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.presentation.components.LocalSnackbarHostState
import com.pobezhkin.learningnews.presentation.home.components.CategoryTabRow
import kotlinx.coroutines.launch
import kotlin.math.abs

/** Главная: вкладки с темами сверху, под ними лента выбранной темы. Темы листаются свайпом. */
@Composable
fun HomeScreen(
    onNewsClick: (String) -> Unit,
    onReadClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val savedUrls by viewModel.savedUrls.collectAsStateWithLifecycle()

    // Сеть пропала в момент сохранения: объясняем, что новость в избранном, а текст придёт позже
    val snackbarHostState = LocalSnackbarHostState.current
    val savedOfflineMessage = stringResource(R.string.snackbar_saved_offline)
    LaunchedEffect(viewModel) {
        viewModel.events.collect { event ->
            when (event) {
                HomeEvent.SavedWithoutNetwork -> {
                    snackbarHostState.currentSnackbarData?.dismiss()
                    snackbarHostState.showSnackbar(
                        message = savedOfflineMessage,
                        duration = SnackbarDuration.Long
                    )
                }
            }
        }
    }

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
                onReadClick = onReadClick,
                onWatchLaterClick = viewModel::onWatchLaterClick
            )
        }
    }
}
