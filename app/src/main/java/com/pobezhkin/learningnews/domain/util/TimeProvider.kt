package com.pobezhkin.learningnews.domain.util

/** Текущее время. Отдельный интерфейс нужен, чтобы в тестах подставлять свои часы. */
fun interface TimeProvider {
    fun currentTimeMillis(): Long
}
