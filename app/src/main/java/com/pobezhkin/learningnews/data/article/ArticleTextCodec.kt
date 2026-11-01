package com.pobezhkin.learningnews.data.article

import com.pobezhkin.learningnews.domain.model.ArticleBlock

/**
 * Как текст статьи лежит в базе: блоки разделены пустой строкой, заголовки подразделов
 * начинаются с «# ». Формат остался прежним, поэтому тексты, сохранённые раньше, читаются без миграции.
 * Абзац, который сам начинается с «# », после чтения из базы станет заголовком: формат это допускает.
 */
object ArticleTextCodec {

    private const val BLOCK_SEPARATOR = "\n\n"
    private const val HEADING_PREFIX = "# "

    fun encode(blocks: List<ArticleBlock>): String =
        blocks.joinToString(separator = BLOCK_SEPARATOR) { block ->
            when (block) {
                is ArticleBlock.Heading -> HEADING_PREFIX + block.text
                is ArticleBlock.Paragraph -> block.text
            }
        }

    fun decode(text: String): List<ArticleBlock> =
        text.split(BLOCK_SEPARATOR)
            .filter { it.isNotBlank() }
            .map { block ->
                if (block.startsWith(HEADING_PREFIX)) {
                    ArticleBlock.Heading(block.removePrefix(HEADING_PREFIX))
                } else {
                    ArticleBlock.Paragraph(block)
                }
            }
}
