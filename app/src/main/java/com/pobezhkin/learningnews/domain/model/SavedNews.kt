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
    /** null, пока текст не загружен. */
    val blocks: List<ArticleBlock>?
)
