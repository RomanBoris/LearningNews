package com.pobezhkin.learningnews.presentation.home

/** Разовые события для экрана: их показывают один раз и не хранят в состоянии. */
sealed interface HomeEvent {

    /** Новость сохранена, но сети нет: текст загрузится позже. */
    data object SavedWithoutNetwork : HomeEvent
}
