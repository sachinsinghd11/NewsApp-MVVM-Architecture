package com.sachin_singh_dighan.newsapp.utils

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        currentRequest.addHeader("X-Api-Key", AppConstant.API_KEY)

        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}