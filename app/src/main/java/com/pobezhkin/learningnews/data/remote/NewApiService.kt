package com.pobezhkin.learningnews.data.remote

import com.pobezhkin.learningnews.data.remote.dto.NewsResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("sources") sources : String,
        @Query("apiKey") apiKey : String

    ): NewsResponseDto
}