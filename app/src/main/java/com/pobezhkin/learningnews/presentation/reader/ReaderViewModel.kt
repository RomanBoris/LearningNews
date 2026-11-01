package com.pobezhkin.learningnews.presentation.reader

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.ArticleTextStatus
import com.pobezhkin.learningnews.domain.model.OnDemandArticle
import com.pobezhkin.learningnews.domain.model.SavedNews
import com.pobezhkin.learningnews.domain.usecase.LoadArticleForReadingUseCase
import com.pobezhkin.learningnews.domain.usecase.ObserveSavedArticleUseCase
import com.pobezhkin.learningnews.presentation.navigation.ARG_NEWS_URL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject

/**
 * Экран чтения открывается из двух мест:
 * - из избранного: текст берётся из базы и работает без сети;
 * - из ленты: новости в избранном нет, поэтому текст скачивается по нажатию.
 */
@HiltViewModel
class ReaderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    observeSavedArticleUseCase: ObserveSavedArticleUseCase,
    private val loadArticleForReadingUseCase: LoadArticleForReadingUseCase
) : ViewModel() {

    private val url: String = savedStateHandle.get<String>(ARG_NEWS_URL).orEmpty()

    /** Счётчик нажатий «Повторить»: каждое новое значение запускает загрузку заново. */
    private val retryCount = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    val uiState: StateFlow<ReaderUiState> = observeSavedArticleUseCase(url)
        // база сообщает об изменении любой сохранённой новости, а нам важна только эта
        .distinctUntilChanged()
        .flatMapLatest { saved ->
            if (saved != null) flowOf(saved.toUiState()) else loadFromFeed()
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS),
            initialValue = ReaderUiState.Loading
        )

    fun onRetryClick() {
        retryCount.update { it + 1 }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadFromFeed(): Flow<ReaderUiState> = retryCount.flatMapLatest {
        flow {
            emit(ReaderUiState.Loading)
            emit(loadArticleForReadingUseCase(url).toUiState())
        }
    }

    private fun SavedNews.toUiState(): ReaderUiState = ReaderUiState.Content(
        news = news,
        text = text,
        availability = when (textStatus) {
            ArticleTextStatus.READY -> TextAvailability.READY
            ArticleTextStatus.PENDING -> TextAvailability.PENDING_NETWORK
            ArticleTextStatus.UNAVAILABLE -> TextAvailability.UNAVAILABLE
        }
    )

    private fun OnDemandArticle.toUiState(): ReaderUiState = when (this) {
        OnDemandArticle.NotFound -> ReaderUiState.NotFound
        is OnDemandArticle.Ready -> ReaderUiState.Content(news, text, TextAvailability.READY)
        is OnDemandArticle.Unavailable -> ReaderUiState.Content(news, null, TextAvailability.UNAVAILABLE)
        is OnDemandArticle.Offline -> ReaderUiState.Content(news, null, TextAvailability.OFFLINE)
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
