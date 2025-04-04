package com.sachin_singh_dighan.newsapp.data.api

import com.sachin_singh_dighan.newsapp.data.model.newsources.NewSourceResponse
import com.sachin_singh_dighan.newsapp.data.model.topheadline.TopHeadLinesResponse
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface NetworkService {

    @GET("top-headlines")
    suspend fun getTopHeadLinesByCategory(@Query("category") category: String): TopHeadLinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadLinesByCountry(@Query("country") country: String): TopHeadLinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadLinesPaginationByCountry(
        @Query("country") country: String,
        @Query("page") page: Int,
        @Query("pageSize") pageSize: Int,
    ): TopHeadLinesResponse

    @GET("top-headlines")
    suspend fun getTopHeadLinesByLanguage(@Query("language") language: String): TopHeadLinesResponse

    @GET("top-headlines/sources")
    suspend fun getNewResources(): NewSourceResponse

    @GET("everything")
    suspend fun getSearchResultForHeadLines(@Query("q") q: String): TopHeadLinesResponse

}