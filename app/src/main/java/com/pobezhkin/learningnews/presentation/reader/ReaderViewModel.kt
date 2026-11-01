package com.pobezhkin.learningnews.presentation.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.ArticleForReading
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.OnDemandArticle
import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.domain.usecase.ObserveArticleForReadingUseCase
import com.pobezhkin.learningnews.presentation.navigation.ARG_NEWS_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Экран чтения открывается из двух мест:
 * - из избранного: текст берётся из базы и работает без сети;
 * - из ленты: новости в избранном нет, поэтому текст скачивается по нажатию.
 * Откуда брать статью, решает [ObserveArticleForReadingUseCase], а здесь она только превращается в состояние экрана.
 */
@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeArticleForReading: ObserveArticleForReadingUseCase
) : ViewModel() {

    private val url: String = savedStateHandle.get<String>(ARG_NEWS_URL).orEmpty()

    /** Счётчик нажатий «Повторить»: каждое новое значение запускает подготовку статьи заново. */
    private val retryCount = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ReaderUiState> = retryCount
        .flatMapLatest { observeArticleForReading(url) }
        .map { article -> article.toUiState() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = ReaderUiState.Loading
        )

    fun onRetryClick() {
        retryCount.update { it + 1 }
    }

    private fun ArticleForReading.toUiState(): ReaderUiState = when (this) {
        ArticleForReading.Loading -> ReaderUiState.Loading
        is ArticleForReading.FromSaved -> saved.toUiState()
        is ArticleForReading.FromFeed -> article.toUiState()
    }

    private fun SavedNews.toUiState(): ReaderUiState = ReaderUiState.Content(
        news = news,
        blocks = blocks.orEmpty(),
        availability = when (textStatus) {
            ArticleTextStatus.READY -> TextAvailability.READY
            ArticleTextStatus.PENDING -> TextAvailability.PENDING_NETWORK
            ArticleTextStatus.UNAVAILABLE -> TextAvailability.UNAVAILABLE
        }
    )

    private fun OnDemandArticle.toUiState(): ReaderUiState = when (this) {
        OnDemandArticle.NotFound -> ReaderUiState.NotFound
        is OnDemandArticle.Ready -> ReaderUiState.Content(news, blocks, TextAvailability.READY)
        is OnDemandArticle.Unavailable -> ReaderUiState.Content(news, emptyList(), TextAvailability.UNAVAILABLE)
        is OnDemandArticle.Offline -> ReaderUiState.Content(news, emptyList(), TextAvailability.OFFLINE)
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
