package com.pobezhkin.learningnews.data.repository

import com.pobezhkin.learningnews.data.article.ArticleTextExtractor
import com.pobezhkin.learningnews.data.network.ArticleClient
import com.pobezhkin.learningnews.domain.model.ArticleDownloadOutcome
import com.pobezhkin.learningnews.domain.model.ArticleFetchResult
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.repository.ArticleTextRepository
import com.pobezhkin.learningnews.domain.repository.WatchLaterRepository
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
    private val watchLaterRepository: WatchLaterRepository
) : ArticleTextRepository {

    override suspend fun fetchText(url: String): ArticleFetchResult =
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
                            val blocks = extractor.extract(html, url)
                            if (blocks != null) ArticleFetchResult.Text(blocks) else ArticleFetchResult.Unavailable
                        }
                        // сервер занят или просит подождать: попробуем позже
                        response.code in RETRYABLE_CODES || response.code >= SERVER_ERROR -> {
                            ArticleFetchResult.NetworkError
                        }
                        // 401, 403, 404, 451 и другие: сайт не отдаёт страницу, повторять нет смысла
                        else -> ArticleFetchResult.Unavailable
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: IOException) {
                // нет сети, оборвалось соединение или вышло время
                ArticleFetchResult.NetworkError
            } catch (e: IllegalArgumentException) {
                // адрес у новости битый (например, без домена)
                ArticleFetchResult.Unavailable
            }
        }

    override suspend fun downloadText(url: String): ArticleDownloadOutcome =
        when (val result = fetchText(url)) {
            is ArticleFetchResult.Text -> {
                watchLaterRepository.updateArticleText(url, ArticleTextStatus.READY, result.blocks)
                ArticleDownloadOutcome.READY
            }
            ArticleFetchResult.Unavailable -> {
                watchLaterRepository.updateArticleText(url, ArticleTextStatus.UNAVAILABLE, null)
                ArticleDownloadOutcome.UNAVAILABLE
            }
            ArticleFetchResult.NetworkError -> ArticleDownloadOutcome.RETRY_LATER
        }

    private companion object {
        val RETRYABLE_CODES = setOf(408, 429)
        const val SERVER_ERROR = 500
    }
}
