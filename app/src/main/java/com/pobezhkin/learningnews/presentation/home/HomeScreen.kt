package com.pobezhkin.learningnews.presentation.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()){
        val state by viewModel.state.collectAsState()

    val _state = state
    when(_state){
        is HomeUiState.Error -> {

            Text("${_state.message}" )

        }
        HomeUiState.Loading -> {
            Box(){ CircularProgressIndicator() }
        }
        is HomeUiState.Success -> {

            LazyColumn() {
                items(_state.news){newsList ->
                    Text(newsList.sourceName, fontSize = 24.sp)
                }
            }

        }
    }
}

