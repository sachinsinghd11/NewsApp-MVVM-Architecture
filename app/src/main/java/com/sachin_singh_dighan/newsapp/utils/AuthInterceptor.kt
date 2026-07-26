package com.sachin_singh_dighan.newsapp.utils

import com.sachin_singh_dighan.newsapp.security.ProductionApiKeyManager
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    private val productionApiKeyManager: ProductionApiKeyManager,
    private val appLogger: Logger
) : Interceptor {

    companion object {
        private const val TAG = "AuthInterceptor"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val currentRequest = chain.request()

        try {
            // Get API key from production manager (most secure method)
            val apiKey = productionApiKeyManager.getProductionApiKey()

            if (apiKey.isBlank()) {
                appLogger.e(TAG, "API key is empty or blank from Production Manager")
                throw RuntimeException("API key not found. Please check production configuration.")
            }

            appLogger.d(TAG, "Retrieved API key from Production Manager: ${apiKey.take(10)}... (Source: ${productionApiKeyManager.getApiKeySource()})")

            // Add API key to request header
            val newRequest = currentRequest.newBuilder()
                .addHeader("X-Api-Key", apiKey)
                .build()

            appLogger.d(TAG, "API key added to request header successfully")

            return chain.proceed(newRequest)

        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to retrieve API key from Production Manager: ${e.message}")
            throw RuntimeException("Failed to retrieve API key from production source", e)
        }
    }
}