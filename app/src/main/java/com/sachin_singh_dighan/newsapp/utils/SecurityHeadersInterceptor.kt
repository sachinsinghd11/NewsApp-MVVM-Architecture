package com.sachin_singh_dighan.newsapp.utils

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class SecurityHeadersInterceptor: Interceptor {

    companion object {
        private const val TAG = "SecurityHeaders"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        // Add security headers to request
        val secureRequest = originalRequest.newBuilder()
            .addHeader("X-Content-Type-Options", "nosniff")
            .addHeader("X-Frame-Options", "DENY")
            .addHeader("X-XSS-Protection", "1; mode=block")
            .addHeader("Referrer-Policy", "strict-origin-when-cross-origin")
            .addHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains")
            .build()

        Log.d(TAG, "Added security headers to request")

        // Proceed with secure request
        val response = chain.proceed(secureRequest)

        // Validate response headers
        validateResponseHeaders(response)

        return response
    }

    /**
     * Validate response headers for security
     */
    private fun validateResponseHeaders(response: Response) {
        val headers = response.headers

        // Check for security headers in response
        val contentTypeOptions = headers["X-Content-Type-Options"]
        val frameOptions = headers["X-Frame-Options"]
        val xssProtection = headers["X-XSS-Protection"]

        if (contentTypeOptions == null || frameOptions == null || xssProtection == null) {
            Log.w(TAG, "Missing security headers in response")
        } else {
            Log.d(TAG, "Response contains security headers")
        }
    }
}