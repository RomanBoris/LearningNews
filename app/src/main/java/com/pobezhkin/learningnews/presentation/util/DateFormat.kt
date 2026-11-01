package com.pobezhkin.learningnews.presentation.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

private const val API_DATE_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'"
private const val DISPLAY_DATE_PATTERN = "d MMM yyyy, HH:mm"

/**
 * Превращает дату из News API ("2026-05-01T12:13:00Z") в вид для экрана ("1 мая 2026, 15:13").
 * Если формат неожиданный — возвращает строку как есть.
 */
fun String.toDisplayDate(): String {
    return try {
        val parser = SimpleDateFormat(API_DATE_PATTERN, Locale.US).apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }
        val date = parser.parse(this) ?: return this
        SimpleDateFormat(DISPLAY_DATE_PATTERN, Locale.getDefault()).format(date)
    } catch (e: ParseException) {
        this
    }
}
