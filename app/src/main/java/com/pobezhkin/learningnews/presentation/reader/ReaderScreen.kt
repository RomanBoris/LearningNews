package com.pobezhkin.learningnews.presentation.reader

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.presentation.util.toDisplayDate

/**
 * Чтение сохранённой статьи. Всё берётся из базы, поэтому экран работает без сети.
 * Если текста пока нет, показываем описание новости и объясняем, почему.
 */
@Composable
fun ReaderScreen(
    onOpenOriginal: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ReaderViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    when (val state = uiState) {
        is ReaderUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }

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
            saved = state.saved,
            onOpenOriginal = { onOpenOriginal(state.saved.news.url) },
            modifier = modifier
        )
    }
}

@Composable
private fun ReaderContent(
    saved: SavedNews,
    onOpenOriginal: () -> Unit,
    modifier: Modifier = Modifier
) {
    val news = saved.news

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
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

        val text = saved.text
        if (saved.textStatus == ArticleTextStatus.READY && !text.isNullOrBlank()) {
            ArticleBody(text)
            Text(
                text = stringResource(R.string.reader_disclaimer),
                modifier = Modifier.padding(top = 24.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            // Полного текста нет: показываем то, что есть, и объясняем, почему так
            news.description?.takeIf { it.isNotBlank() }?.let { description ->
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            MissingTextNote(
                status = saved.textStatus,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        Button(
            onClick = onOpenOriginal,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text(stringResource(R.string.reader_open_original))
        }
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

@Composable
private fun MissingTextNote(
    status: ArticleTextStatus,
    modifier: Modifier = Modifier
) {
    val noteRes = when (status) {
        ArticleTextStatus.UNAVAILABLE -> R.string.reader_note_unavailable
        else -> R.string.reader_note_pending
    }
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh
    ) {
        Text(
            text = stringResource(noteRes),
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

/** Автор и дата в одну строку. Автором иногда приходит ссылка на сайт: такое не показываем. */
private fun readerMeta(author: String?, date: String): String {
    val authorName = author?.takeIf { it.isNotBlank() && !it.startsWith("http") }
    return if (authorName != null) "$authorName  ·  $date" else date
}
