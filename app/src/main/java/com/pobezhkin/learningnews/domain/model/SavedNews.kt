package com.pobezhkin.learningnews.domain.model

/** Что с текстом сохранённой новости. */
enum class ArticleTextStatus {
    /** Текст ещё не загружен: ждём сеть или идёт загрузка. */
    PENDING,

    /** Текст загружен и лежит в базе, читать можно без сети. */
    READY,

    /** Сайт не отдал текст (защита от ботов, платный доступ, страница без статьи). */
    UNAVAILABLE
}

/** Новость из «Смотреть позже» вместе с сохранённым текстом, если он уже загружен. */
data class SavedNews(
    val news: News,
    val textStatus: ArticleTextStatus,
    /** Абзацы разделены пустой строкой, заголовки подразделов начинаются с «# ». */
    val text: String?
)
