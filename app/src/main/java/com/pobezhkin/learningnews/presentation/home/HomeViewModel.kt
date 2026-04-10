package com.pobezhkin.learningnews.presentation.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pobezhkin.learningnews.domain.usecase.GetTopHeadlinesUseCase
import com.pobezhkin.learningnews.domain.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getTopHeadlinesUseCase: GetTopHeadlinesUseCase,
) : ViewModel() {
    private val _state = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val state: StateFlow<HomeUiState> = _state.asStateFlow()


    init {
        loadNews()
    }

    private fun loadNews() {

        viewModelScope.launch {

            when(val result = getTopHeadlinesUseCase()){
                        is Result.Success -> _state.value = HomeUiState.Success(result.data)
                        is Result.Error -> _state.value = HomeUiState.Error(result.message)
            }


        }
    }

}