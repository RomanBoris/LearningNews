package com.pobezhkin.learningnews.presentation.later.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.R
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.SavedNews

/**
 * Нижняя часть карточки в избранном: что с текстом статьи и выбор «читать здесь или на сайте».
 * «Читать» открывает сохранённый текст (работает без сети). Пока текста нет, на этой кнопке
 * «Кратко»: на экране чтения будет хотя бы описание новости.
 */
@Composable
fun ArticleActions(
    saved: SavedNews,
    onReadClick: () -> Unit,
    onOriginalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = saved.textStatus == ArticleTextStatus.READY

    Column(modifier = modifier.padding(top = 16.dp)) {
        HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

        TextStatusLine(
            status = saved.textStatus,
            modifier = Modifier.padding(top = 12.dp)
        )

        Row(
            modifier = Modifier.padding(top = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilledTonalButton(onClick = onReadClick) {
                Text(stringResource(if (isReady) R.string.action_read else R.string.action_read_short))
            }
            OutlinedButton(onClick = onOriginalClick) {
                Text(stringResource(R.string.action_open_original))
            }
        }
    }
}

@Composable
private fun TextStatusLine(
    status: ArticleTextStatus,
    modifier: Modifier = Modifier
) {
    val (icon, textRes) = statusPresentation(status)
    val color = if (status == ArticleTextStatus.READY) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = color
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = stringResource(textRes),
            style = MaterialTheme.typography.labelMedium,
            color = color
        )
    }
}

private fun statusPresentation(status: ArticleTextStatus): Pair<ImageVector, Int> = when (status) {
    ArticleTextStatus.READY -> Icons.Default.Check to R.string.article_status_ready
    ArticleTextStatus.PENDING -> Icons.Default.Info to R.string.article_status_pending
    ArticleTextStatus.UNAVAILABLE -> Icons.Default.Warning to R.string.article_status_unavailable
}
