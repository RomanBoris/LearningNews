package com.pobezhkin.learningnews.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.presentation.util.toDisplayDate
import com.pobezhkin.learningnews.ui.theme.LearningNewsTheme

@Composable
fun NewsCard(
    news: News,
    isSaved: Boolean,
    onWatchLaterClick: () -> Unit,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    /** Доп. содержимое внизу карточки: например, кнопки «Читать» и «Оригинал» в избранном. */
    extraContent: (@Composable ColumnScope.() -> Unit)? = null
) {
    Card(
        onClick = { onClick(news.url) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    ) {
        Box {
            NewsImage(
                url = news.urlToImage,
                sourceName = news.sourceName,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
            )

            // Ключ новости — её url, без него сохранить нельзя
            if (news.url.isNotBlank()) {
                WatchLaterButton(
                    isSaved = isSaved,
                    onClick = onWatchLaterClick,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(10.dp)
                )
            }
        }

        Column(modifier = Modifier.padding(16.dp)) {
            if (news.sourceName.isNotBlank()) {
                Text(
                    text = news.sourceName.uppercase(),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.secondary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Text(
                text = news.title,
                modifier = Modifier.padding(top = 6.dp),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 4,
                overflow = TextOverflow.Ellipsis
            )

            news.description?.takeIf { it.isNotBlank() }?.let { description ->
                Text(
                    text = description,
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }

            NewsMeta(
                author = news.author,
                publishedAt = news.publishedAt,
                modifier = Modifier.padding(top = 12.dp)
            )

            extraContent?.invoke(this)
        }
    }
}

/** Автор и дата в одну строку. Автором иногда приходит ссылка на сайт: такое не показываем. */
@Composable
private fun NewsMeta(
    author: String?,
    publishedAt: String,
    modifier: Modifier = Modifier
) {
    val authorName = author?.takeIf { it.isNotBlank() && !it.startsWith("http") }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        if (authorName != null) {
            Text(
                text = authorName,
                modifier = Modifier.weight(1f, fill = false),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "  ·  ",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Text(
            text = publishedAt.toDisplayDate(),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1
        )
    }
}

/** Сердечко на картинке. Полупрозрачная тёмная подложка читается на любом фоне. */
@Composable
private fun WatchLaterButton(
    isSaved: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    FilledIconButton(
        onClick = onClick,
        modifier = modifier,
        colors = IconButtonDefaults.filledIconButtonColors(
            containerColor = Color.Black.copy(alpha = 0.45f),
            contentColor = Color.White
        )
    ) {
        Icon(
            imageVector = if (isSaved) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = stringResource(
                if (isSaved) R.string.watch_later_remove else R.string.watch_later_add
            ),
            tint = if (isSaved) MaterialTheme.colorScheme.secondary else Color.White
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun NewsCardPreview() {
    LearningNewsTheme {
        NewsCard(
            news = News(
                id = "1",
                title = "NASA discovers water on Mars surface",
                author = "John Smith",
                description = "Scientists have found evidence of liquid water beneath the Martian surface, raising new questions about the possibility of life.",
                url = "https://example.com/news/1",
                urlToImage = null,
                publishedAt = "2026-05-01T12:13:00Z",
                sourceName = "BBC Science"
            ),
            isSaved = true,
            onWatchLaterClick = {}
        )
    }
}
