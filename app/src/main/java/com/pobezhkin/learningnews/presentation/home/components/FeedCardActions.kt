package com.pobezhkin.learningnews.presentation.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.pobezhkin.learningnews.R

/**
 * Кнопки внизу карточки в ленте: прочитать статью в приложении или открыть её на сайте.
 * «Читать» скачивает текст по нажатию. Если источник его не отдаёт или нет сети,
 * экран чтения покажет краткое описание и предупредит об этом.
 */
@Composable
fun FeedCardActions(
    onReadClick: () -> Unit,
    onOriginalClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.padding(top = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        FilledTonalButton(onClick = onReadClick) {
            Text(stringResource(R.string.action_read))
        }
        OutlinedButton(onClick = onOriginalClick) {
            Text(stringResource(R.string.action_open_original))
        }
    }
}
