package com.pobezhkin.learningnews.presentation.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.presentation.util.toDisplayDate

/**
 * Чтение статьи. Из избранного всё берётся из базы и работает без сети.
 * Из ленты текст скачивается по нажатию. Если полного текста нет, показываем краткое
 * описание и предупреждаем, почему так.
 */
@Composable
fun ReaderScreen(
    onOpenOriginal: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ReaderUiState.Loading -> LoadingContent(modifier)

        is ReaderUiState.NotFound -> Box(
            modifier = modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = stringResource(R.string.reader_not_found),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        is ReaderUiState.Content -> ReaderContent(
            state = state,
            onOpenOriginal = { onOpenOriginal(state.news.url) },
            onRetryClick = viewModel::onRetryClick,
            modifier = modifier
        )
    }
}

@Composable
private fun LoadingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text(
            text = stringResource(R.string.reader_loading),
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ReaderContent(
    state: ReaderUiState.Content,
    onOpenOriginal: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val news = state.news

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        ArticleHeader(news)

        val text = state.text
        if (state.availability == TextAvailability.READY && !text.isNullOrBlank()) {
            ArticleBody(text)
            Text(
                text = stringResource(R.string.reader_disclaimer),
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Полного текста нет: показываем краткое описание и объясняем, почему так
            news.description?.takeIf { it.isNotBlank() }?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            MissingTextNote(
                availability = state.availability,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        if (state.availability == TextAvailability.OFFLINE) {
            OutlinedButton(
                onClick = onRetryClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(stringResource(R.string.action_retry))
            }
        }

        Button(
            onClick = onOpenOriginal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(stringResource(R.string.reader_open_original))
        }
    }
}

@Composable
private fun ArticleHeader(news: News) {
    Column {
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
            modifier = Modifier.padding(top = 8.dp),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 24.sp,
                lineHeight = 32.sp
            )
        )

        Text(
            text = readerMeta(news.author, news.publishedAt.toDisplayDate()),
            modifier = Modifier.padding(top = 12.dp),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 16.dp),
            color = MaterialTheme.colorScheme.outlineVariant
        )
    }
}

/** Абзацы разделены пустой строкой, заголовки подразделов помечены «# » в начале. */
@Composable
private fun ArticleBody(text: String) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        text.split("\n\n").forEach { block ->
            if (block.startsWith("# ")) {
                Text(
                    text = block.removePrefix("# "),
                    modifier = Modifier.padding(top = 8.dp),
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontSize = 19.sp,
                        lineHeight = 26.sp
                    )
                )
            } else {
                Text(
                    text = block,
                    style = MaterialTheme.typography.bodyLarge.copy(lineHeight = 27.sp)
                )
            }
        }
    }
}

/** Заметка о том, почему полного текста нет. Предупреждение заметное: значок и цветная плашка. */
@Composable
private fun MissingTextNote(
    availability: TextAvailability,
    modifier: Modifier = Modifier
) {
    val isWarning = availability != TextAvailability.PENDING_NETWORK
    val noteRes = when (availability) {
        TextAvailability.PENDING_NETWORK -> R.string.reader_note_pending
        TextAvailability.OFFLINE -> R.string.reader_note_offline
        else -> R.string.reader_note_unavailable
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isWarning) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHigh
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = if (isWarning) Icons.Default.Warning else Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (isWarning) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
            Text(
                text = stringResource(noteRes),
                modifier = Modifier.padding(start = 12.dp),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isWarning) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                }
            )
        }
    }
}

/** Автор и дата в одну строку. Автором иногда приходит ссылка на сайт: такое не показываем. */
private fun readerMeta(author: String?, date: String): String {
    val authorName = author?.takeIf { it.isNotBlank() && !it.startsWith("http") }
    return if (authorName != null) "$authorName  ·  $date" else date
}
