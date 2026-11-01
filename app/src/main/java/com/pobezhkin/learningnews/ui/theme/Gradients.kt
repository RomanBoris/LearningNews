package com.pobezhkin.learningnews.ui.theme

import androidx.compose.ui.graphics.Brush

/**
 * Градиенты приложения. Шапка и ряд вкладок стоят друг под другом и вместе дают один плавный
 * переход «тёмный → фиолетовый», как на референсе.
 */
object NewsGradients {

    /** Верхняя панель. */
    val TopBar: Brush = Brush.verticalGradient(listOf(HeaderTop, HeaderMiddle))

    /** Ряд вкладок под верхней панелью: продолжает градиент с того же цвета. */
    val Tabs: Brush = Brush.verticalGradient(listOf(HeaderMiddle, HeaderBottom))

    /** Фон картинки, пока она грузится или её нет. */
    val ImagePlaceholder: Brush = Brush.linearGradient(listOf(Slate, Indigo, Violet))
}
