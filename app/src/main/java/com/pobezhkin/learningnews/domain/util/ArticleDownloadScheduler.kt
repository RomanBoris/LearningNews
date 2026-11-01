package com.pobezhkin.learningnews.domain.util

/** Ставит загрузку текста статьи в очередь. Задача сама дождётся сети и переживёт перезапуск. */
interface ArticleDownloadScheduler {

    /** Повторный вызов для того же адреса ничего не дублирует. */
    fun schedule(url: String)

    fun cancel(url: String)
}
