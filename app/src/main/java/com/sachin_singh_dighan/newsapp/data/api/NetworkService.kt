package com.sachin_singh_dighan.newsapp.data.api

import com.sachin_singh_dighan.newsapp.AppConstant
import com.sachin_singh_dighan.newsapp.data.model.new_sources.NewSourceResponse
import com.sachin_singh_dighan.newsapp.data.model.top_headline.TopHeadLinesResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @Headers("X-Api-Key: ${AppConstant.API_KEY}")
    @GET("top-headlines")
    suspend fun getTopHeadLinesByCountry(@Query("country") country: String): TopHeadLinesResponse

    @Headers("X-Api-Key: ${AppConstant.API_KEY}")
    @GET("top-headlines")
    suspend fun getTopHeadLinesByLanguage(@Query("language") language: String): TopHeadLinesResponse

    @Headers("X-Api-Key: ${AppConstant.API_KEY}")
    @GET("top-headlines/sources")
    suspend fun getNewResources(): NewSourceResponse

}