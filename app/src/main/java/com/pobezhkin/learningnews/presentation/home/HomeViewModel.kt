package com.pobezhkin.learningnews.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase
): ViewModel() {
        private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state : StateFlow<HomeUiState> = _state.asStateFlow()



    init{
        loadNews()
    }

    private fun loadNews(){

            viewModelScope.launch {
                try {
                    val news = getTopHeadlinesUseCase()
                    _state.value = HomeUiState.Success(news = news)
                }catch (e: IOException){
                    _state.value = HomeUiState.Error(e.toString())

                }


            }
    }

}