package com.pobezhkin.learningnews.presentation.home.components

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.domain.model.NewsCategory
import com.pobezhkin.learningnews.presentation.home.titleRes
import com.pobezhkin.learningnews.ui.theme.NewsGradients

/**
 * Ряд вкладок с темами на градиенте, который продолжает верхнюю панель.
 * Тем больше, чем влезает в экран, поэтому ряд прокручивается сам.
 */
@Composable
fun CategoryTabRow(
    categories: List<NewsCategory>,
    selectedIndex: Int,
    onCategoryClick: (index: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedColor = MaterialTheme.colorScheme.onSurface
    val unselectedColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.65f)

    ScrollableTabRow(
        selectedTabIndex = selectedIndex,
        modifier = modifier.background(NewsGradients.Tabs),
        containerColor = Color.Transparent,
        contentColor = selectedColor,
        edgePadding = 12.dp,
        divider = {}
    ) {
        categories.forEachIndexed { index, category ->
            val selected = index == selectedIndex
            Tab(
                selected = selected,
                onClick = { onCategoryClick(index) },
                selectedContentColor = selectedColor,
                unselectedContentColor = unselectedColor,
                text = {
                    Text(
                        text = stringResource(category.titleRes()),
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            )
        }
    }
}
