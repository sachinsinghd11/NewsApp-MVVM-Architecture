package com.sachin_singh_dighan.newsapp.utils

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.URL
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

class NetworkSecurityValidator {
    companion object {
        private const val TAG = "NetworkSecurity"
        private const val MAX_REQUESTS_PER_MINUTE = 60
        private val SUSPICIOUS_PATTERNS = listOf(
            "javascript:", "data:", "vbscript:", "onload=", "onerror="
        )
    }

    // Rate limiting per host
    private val requestCounts = ConcurrentHashMap<String, AtomicInteger>()

    /**
     * Validate request URL for security
     */
    fun validateRequestUrl(url: String): Boolean {
        try {
            val parsedUrl = URL(url)

            // Check for suspicious patterns
            if (SUSPICIOUS_PATTERNS.any { url.contains(it, ignoreCase = true) }) {
                Log.w(TAG, "Suspicious pattern detected in URL: $url")
                return false
            }

            // Validate protocol
            if (parsedUrl.protocol != "https") {
                Log.w(TAG, "Non-HTTPS protocol detected: ${parsedUrl.protocol}")
                return false
            }

            // Validate host
            if (!isValidHost(parsedUrl.host)) {
                Log.w(TAG, "Invalid host detected: ${parsedUrl.host}")
                return false
            }

            return true
        } catch (e: Exception) {
            Log.e(TAG, "Error validating URL: ${e.message}")
            return false
        }
    }

    /**
     * Check rate limiting
     */
    fun isRateLimitExceeded(host: String): Boolean {
        val count = requestCounts.getOrPut(host) { AtomicInteger(0) }
        val currentCount = count.incrementAndGet()

        if (currentCount > MAX_REQUESTS_PER_MINUTE) {
            Log.w(TAG, "Rate limit exceeded for host: $host")
            return true
        }

        // Reset counter after 1 minute
        CoroutineScope(kotlinx.coroutines.Dispatchers.IO).launch {
            delay(60000) // 1 minute
            count.set(0)
        }

        return false
    }

    /**
     * Validate host
     */
    private fun isValidHost(host: String): Boolean {
        // Allow only newsapi.org and subdomains
        return host == "newsapi.org" || host.endsWith(".newsapi.org")
    }

    /**
     * Reset rate limiting for testing
     */
    fun resetRateLimiting() {
        requestCounts.clear()
        Log.d(TAG, "Rate limiting reset")
    }
}