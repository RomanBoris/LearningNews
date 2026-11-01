package com.pobezhkin.learningnews.presentation.components

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Общий снекбар приложения. Его хост стоит в Scaffold (NewsApp), а показывать сообщения
 * могут любые экраны, не передавая хост через каждый уровень вложенности.
 */
val LocalSnackbarHostState = staticCompositionLocalOf<SnackbarHostState> {
    error("SnackbarHostState не передан: оберните экран в CompositionLocalProvider")
}
