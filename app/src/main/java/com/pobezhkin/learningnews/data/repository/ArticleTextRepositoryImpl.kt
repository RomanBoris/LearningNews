package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.data.article.ArticleTextExtractor
import com.pobezhkin.learningnews.data.local.dao.WatchLaterDao
import com.pobezhkin.learningnews.di.ArticleClient
import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.repository.ArticleTextRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import javax.inject.Inject

class ArticleTextRepositoryImpl @Inject constructor(
    @ArticleClient private val httpClient: OkHttpClient,
    private val extractor: ArticleTextExtractor,
    private val watchLaterDao: WatchLaterDao
) : ArticleTextRepository {

    override suspend fun downloadText(url: String): ArticleDownloadOutcome =
        withContext(Dispatchers.IO) {
            try {
                val request = Request.Builder()
                    .url(url)
                    .header("Accept", "text/html")
                    .build()
                httpClient.newCall(request).execute().use { response ->
                    when {
                        response.isSuccessful -> {
                            val html = response.body?.string().orEmpty()
                            store(url, extractor.extract(html, url))
                        }
                        // сервер занят или просит подождать: попробуем позже
                        response.code in RETRYABLE_CODES || response.code >= SERVER_ERROR -> {
                            ArticleDownloadOutcome.RETRY_LATER
                        }
                        // 401, 403, 404, 451 и другие: сайт не отдаёт страницу, повторять нет смысла
                        else -> store(url, null)
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                // нет сети, оборвалось соединение или вышло время: дождёмся сети
                ArticleDownloadOutcome.RETRY_LATER
            } catch (e: IllegalArgumentException) {
                // адрес у новости битый (например, без домена)
                store(url, null)
            }
        }

    private suspend fun store(url: String, text: String?): ArticleDownloadOutcome =
        if (text != null) {
            watchLaterDao.updateContent(url, ArticleTextStatus.READY.name, text)
            ArticleDownloadOutcome.READY
        } else {
            watchLaterDao.updateContent(url, ArticleTextStatus.UNAVAILABLE.name, null)
            ArticleDownloadOutcome.UNAVAILABLE
        }

    private companion object {
        val RETRYABLE_CODES = setOf(408, 429)
        const val SERVER_ERROR = 500
    }
}
