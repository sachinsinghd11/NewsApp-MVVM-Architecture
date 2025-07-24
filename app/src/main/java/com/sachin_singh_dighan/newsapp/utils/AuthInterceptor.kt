package com.sachin_singh_dighan.newsapp.utils

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request().newBuilder()
        
        // Use API key from encrypted storage
        try {
            val apiKeyManager = EncryptedApiKeyManager(context)
            val apiKey = apiKeyManager.getApiKey()
            
            if (apiKey.isNotEmpty()) {
                currentRequest.addHeader("X-Api-Key", apiKey)
            } else {
                throw RuntimeException("API key not found in encrypted storage. Please check configuration.")
            }
        } catch (e: Exception) {
            // Log error and throw runtime exception
            throw RuntimeException("Failed to retrieve API key from encrypted storage", e)
        }
        
        val newRequest = currentRequest.build()
        return chain.proceed(newRequest)
    }
}