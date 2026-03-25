package com.pobezhkin.learningnews.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.model.News
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class HomeViewModel(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
): ViewModel() {
        private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state : StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            try {
                val news = getTopHeadlinesUseCase()
                _state.value = HomeUiState.Success(news = news)
            }catch (e:  Exception){
                _state.value = HomeUiState.Error()

            }


        }
    }

}