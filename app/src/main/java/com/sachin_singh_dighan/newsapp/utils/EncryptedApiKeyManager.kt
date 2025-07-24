package com.sachin_singh_dighan.newsapp.utils

import android.content.Context
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.sachin_singh_dighan.newsapp.BuildConfig
import com.sachin_singh_dighan.newsapp.utils.logger.Logger

/**
 * Secure API Key Manager using Encrypted SharedPreferences
 * Provides encrypted storage for sensitive data like API keys
 */
class EncryptedApiKeyManager(private val context: Context) {
    
    companion object {
        private const val PREFS_NAME = "secure_api_keys"
        private const val KEY_NEWS_API_KEY = "news_api_key"
        private const val KEY_BASE_URL = "base_url"
    }
    
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()
    
    private val sharedPreferences = EncryptedSharedPreferences.create(
        context, 
        PREFS_NAME, 
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    
    /**
     * Store API key securely
     */
    fun storeApiKey(apiKey: String) {
        try {
            sharedPreferences.edit().putString(KEY_NEWS_API_KEY, apiKey).apply()
            Log.d("EncryptedApiKeyManager", "API key stored successfully")
        } catch (e: Exception) {
            Log.e("EncryptedApiKeyManager", "Failed to store API key: ${e.message}")
            throw RuntimeException("Failed to store API key securely", e)
        }
    }
    
    /**
     * Get API key securely
     */
    fun getApiKey(): String {
        return try {
            sharedPreferences.getString(KEY_NEWS_API_KEY, "") ?: ""
        } catch (e: Exception) {
            Log.e("EncryptedApiKeyManager", "Failed to retrieve API key: ${e.message}")
            ""
        }
    }
    
    /**
     * Store base URL securely
     */
    fun storeBaseUrl(baseUrl: String) {
        try {
            sharedPreferences.edit().putString(KEY_BASE_URL, baseUrl).apply()
            Log.d("EncryptedApiKeyManager", "Base URL stored successfully")
        } catch (e: Exception) {
            Log.e("EncryptedApiKeyManager", "Failed to store base URL: ${e.message}")
        }
    }
    
    /**
     * Get base URL securely
     */
    fun getBaseUrl(): String {
        return try {
            sharedPreferences.getString(KEY_BASE_URL, BuildConfig.BASE_URL) ?: BuildConfig.BASE_URL
        } catch (e: Exception) {
            Log.e("EncryptedApiKeyManager", "Failed to retrieve base URL: ${e.message}")
            BuildConfig.BASE_URL
        }
    }
    
    /**
     * Check if API key exists
     */
    fun hasApiKey(): Boolean {
        return getApiKey().isNotEmpty()
    }
    
    /**
     * Clear all stored data
     */
    fun clearAll() {
        try {
            sharedPreferences.edit().clear().apply()
            Log.d("EncryptedApiKeyManager", "All stored data cleared")
        } catch (e: Exception) {
            Log.e("EncryptedApiKeyManager", "Failed to clear data: ${e.message}")
        }
    }
    
    /**
     * Validate API key format
     */
    fun isApiKeyValid(apiKey: String): Boolean {
        return apiKey.isNotEmpty() && apiKey.length >= 10
    }
    
    /**
     * Initialize with default values if empty
     */
    fun initializeIfNeeded() {
        if (!hasApiKey()) {
            val defaultApiKey = if (BuildConfig.DEBUG) {
                BuildConfig.NEWS_API_KEY
            } else {
                // For release builds, you might want to get from secure source
                getApiKeyFromSecureSource()
            }
            
            if (isApiKeyValid(defaultApiKey)) {
                storeApiKey(defaultApiKey)
                Log.d("EncryptedApiKeyManager", "API key initialized with default value")
            } else {
                Log.w("EncryptedApiKeyManager", "No valid API key found for initialization")
            }
        }
    }
    
    /**
     * Get API key from secure source (override this for production)
     */
    private fun getApiKeyFromSecureSource(): String {
        // In production, this could be:
        // 1. Fetched from your backend server
        // 2. Obtained from user input
        // 3. Retrieved from Google Cloud Secret Manager
        // 4. Generated dynamically
        
        return "5e59e08053df4d13a5d4ff6fb1b228d0" // Default for demo
    }
} 