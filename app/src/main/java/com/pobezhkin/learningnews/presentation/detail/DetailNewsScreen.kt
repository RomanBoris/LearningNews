package com.pobezhkin.learningnews.presentation.detail

import android.annotation.SuppressLint
import android.net.Uri
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun DetailNewsScreen(newsUrl: String) {
    val decoderUrl = Uri.decode(newsUrl)
    AndroidView(
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                webViewClient = WebViewClient()
                loadUrl(decoderUrl)
            }

        },
        modifier = Modifier.fillMaxSize()
    )
}