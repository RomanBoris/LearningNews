package com.pobezhkin.learningnews.domain.util

/** Есть ли сейчас интернет. Домен не знает про Android, поэтому спрашивает через интерфейс. */
interface NetworkMonitor {
    fun isOnline(): Boolean
}
