package com.pobezhkin.learningnews.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.pobezhkin.learningnews.ui.theme.NewsGradients

/**
 * Картинка новости. Градиент лежит под ней всегда: виден, пока картинка грузится.
 * Если картинки нет или она не загрузилась, на градиенте остаётся первая буква источника.
 * Часть сайтов (CNBC, Kotaku, WSJ и другие) не отдаёт картинки приложениям, поэтому такое случается.
 */
@Composable
fun NewsImage(
    url: String?,
    sourceName: String,
    modifier: Modifier = Modifier
) {
    var failed by remember(url) { mutableStateOf(false) }
    val hasImage = !url.isNullOrBlank() && !failed

    Box(
        modifier = modifier.background(NewsGradients.ImagePlaceholder),
        contentAlignment = Alignment.Center
    ) {
        if (hasImage) {
            val context = LocalContext.current
            val request = remember(url) {
                ImageRequest.Builder(context)
                    .data(url)
                    .crossfade(true)
                    .build()
            }
            AsyncImage(
                model = request,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                onError = { failed = true }
            )
        } else {
            Text(
                text = sourceName.firstOrNull { it.isLetterOrDigit() }?.uppercase() ?: "N",
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.35f),
                fontFamily = FontFamily.Serif,
                fontWeight = FontWeight.Bold,
                fontSize = 56.sp
            )
        }
    }
}
