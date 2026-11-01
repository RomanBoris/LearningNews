package com.pobezhkin.learningnews.presentation.home.components

import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.presentation.home.titleRes

/** Ряд вкладок с темами. Тем больше, чем влезает в экран, поэтому ряд прокручивается сам. */
@Composable
fun CategoryTabRow(
    categories: List<NewsCategory>,
    selectedIndex: Int,
    onCategoryClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier,
        edgePadding = 8.dp
    ) {
        categories.forEachIndexed { index, category ->
            Tab(
                selected = index == selectedIndex,
                onClick = { onCategoryClick(index) },
                text = { Text(stringResource(category.titleRes())) }
            )
        }
    }
}
