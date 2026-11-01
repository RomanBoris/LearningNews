package com.pobezhkin.learningnews.data.article

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import javax.inject.Inject

/**
 * Достаёт из HTML-страницы статью: заголовки подразделов и абзацы, без рекламы, меню и скриптов.
 * Результат — обычный текст: абзацы разделены пустой строкой, заголовки подразделов начинаются с «# ».
 */
class ArticleTextExtractor @Inject constructor() {

    /** Возвращает текст статьи или null, если на странице нормального текста нет. */
    fun extract(html: String, baseUrl: String): String? {
        val document = Jsoup.parse(html, baseUrl)
        document.select(NOISE_SELECTOR).remove()

        val text = findArticleRoot(document)
            .select("h2, h3, p")
            .mapNotNull(::toBlock)
            .distinct()
            .joinToString(separator = "\n\n")

        return text.takeIf { it.length >= MIN_TEXT_LENGTH }?.take(MAX_TEXT_LENGTH)
    }

    /** Берём блок, в котором больше всего абзацев. Если подходящего нет, ищем по всей странице. */
    private fun findArticleRoot(document: Document): Element {
        val best = document.select(ARTICLE_SELECTOR).maxByOrNull(::paragraphsLength)
        return if (best != null && paragraphsLength(best) >= MIN_TEXT_LENGTH) best else document.body()
    }

    private fun paragraphsLength(element: Element): Int =
        element.select("p").sumOf { it.text().length }

    private fun toBlock(element: Element): String? {
        val text = element.text().trim()
        if (isWidgetStub(text)) return null
        return when (element.tagName()) {
            "p" -> text.takeIf { it.length >= MIN_PARAGRAPH_LENGTH }
            else -> text.takeIf { it.length in MIN_HEADING_LENGTH..MAX_HEADING_LENGTH }?.let { "# $it" }
        }
    }

    /** Заглушки аудиоплеера вроде «Getting your Trinity Audio player ready...» — не часть статьи. */
    private fun isWidgetStub(text: String): Boolean =
        text.contains("Trinity Audio", ignoreCase = true) || AUDIO_PLAYER_STUB.containsMatchIn(text)

    private companion object {
        const val NOISE_SELECTOR = "script, style, noscript, nav, header, footer, aside, form, " +
            "iframe, figure, figcaption, button, svg, [role=navigation], [role=complementary]"
        const val ARTICLE_SELECTOR = "article, [itemprop=articleBody], main"

        val AUDIO_PLAYER_STUB = Regex("^Getting your .*player ready", RegexOption.IGNORE_CASE)

        /** Короткие «абзацы» — это подписи и кнопки, в статью они не входят. */
        const val MIN_PARAGRAPH_LENGTH = 40
        const val MIN_HEADING_LENGTH = 3
        const val MAX_HEADING_LENGTH = 200

        /** Меньше этого — скорее всего страница из скриптов или закрыта платным доступом. */
        const val MIN_TEXT_LENGTH = 400
        const val MAX_TEXT_LENGTH = 100_000
    }
}
