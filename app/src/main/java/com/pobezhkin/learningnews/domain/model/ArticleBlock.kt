package com.pobezhkin.learningnews.domain.model

/** Кусок текста статьи: заголовок подраздела или абзац. */
sealed interface ArticleBlock {
    val text: String

    data class Heading(override val text: String) : ArticleBlock

    data class Paragraph(override val text: String) : ArticleBlock
}
