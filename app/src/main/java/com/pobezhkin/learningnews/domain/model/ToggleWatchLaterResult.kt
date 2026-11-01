package com.pobezhkin.learningnews.domain.model

/** Что произошло после нажатия на сердечко. */
sealed interface ToggleWatchLaterResult {

    /**
     * Новость сохранена в избранное, загрузка её текста поставлена в очередь.
     * [isOffline] = true, если сети сейчас нет: текст загрузится позже, когда она появится.
     */
    data class Saved(val isOffline: Boolean) : ToggleWatchLaterResult

    /** Новость убрана из избранного. */
    data object Removed : ToggleWatchLaterResult
}
