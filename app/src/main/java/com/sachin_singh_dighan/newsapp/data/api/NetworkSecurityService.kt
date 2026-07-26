package com.sachin_singh_dighan.newsapp.data.api

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.sachin_singh_dighan.newsapp.BuildConfig
import com.sachin_singh_dighan.newsapp.utils.NetworkHelper
import com.sachin_singh_dighan.newsapp.utils.NetworkSecurityValidator
import com.sachin_singh_dighan.newsapp.utils.SecurityConfig
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import javax.inject.Inject
import javax.inject.Singleton


// At top of NetworkSecurityService.kt (or a nearby file)
enum class Endpoint(val path: String) {
    TopHeadlines("top-headlines"),
    Everything("everything"),
    Sources("top-headlines/sources")
}
@Singleton
class NetworkSecurityService @Inject constructor(
    private val networkSecurityValidator: NetworkSecurityValidator,
    private val networkHelper: NetworkHelper,
    private val securityConfig: SecurityConfig,
    private val appLogger: Logger
){
    companion object{
        private const val TAG = "NetworkSecurityService"
    }

    /**
     * Initialize network security
     */
    fun initializeNetworkSecurity(): Boolean {
        return securityConfig.initializeSecurity()
    }

    /**
     * Get security status
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresPermission(Manifest.permission.USE_BIOMETRIC)
    fun getSecurityStatus(): String {
        return securityConfig.getSecurityStatus()
    }

    fun validateApiRequest(apiEndpoint: String): Boolean {
        try {
            // Map free text to a known path (prevents "Top Headlines" bug)
            val endpoint = when (apiEndpoint.trim().lowercase()) {
                "topheadlines", "top-headlines", "top headlines" -> Endpoint.TopHeadlines
                "everything" -> Endpoint.Everything
                "sources", "top-headlines/sources" -> Endpoint.Sources
                else -> {
                    appLogger.e(TAG, "Unknown endpoint label: $apiEndpoint")
                    return false
                }
            }

            val base = BuildConfig.BASE_URL
            val baseHttp = base.toHttpUrlOrNull()
            if (baseHttp == null) {
                appLogger.e(TAG, "Invalid BASE_URL: $base")
                return false
            }

            // Build full URL with safe path (no spaces)
            val completeUrl = baseHttp.newBuilder()
                .addPathSegments(endpoint.path) // handles slashes & encoding
                .build()
                .toString()

            appLogger.d(TAG, "Validating API request for endpoint: ${endpoint.name}")
            appLogger.d(TAG, "Full URL: $completeUrl")

            if (!networkHelper.isNetworkAvailable()) {
                appLogger.w(TAG, "Network not available for API request")
                return false
            }

            // Don’t hard-fail dev just because key isn’t persisted yet (your SecurityConfig can relax in debug)
            if (!securityConfig.validateSecurity()) {
                appLogger.e(TAG, "Security configuration validation failed")
                return false
            }

            if (!networkSecurityValidator.validateRequestUrl(completeUrl)) {
                appLogger.e(TAG, "URL security validation failed for: $completeUrl")
                return false
            }

            // ✅ Fix the parameter check:
            // NewsAPI /v2/top-headlines requires at least ONE of: country | category | sources | q
            /*if (endpoint == Endpoint.TopHeadlines) {
                val q = completeUrl.substringAfter('?', "")
                val hasCountry  = q.contains("country=")
                val hasCategory = q.contains("category=")
                val hasSources  = q.contains("sources=")
                val hasQuery    = q.contains("q=")
                if (!(hasCountry || hasCategory || hasSources || hasQuery)) {
                    appLogger.w(TAG, "One of 'country', 'category', 'sources', or 'q' is required for top-headlines")
                    return false
                }
            }*/

            appLogger.d(TAG, "API request validation successful for ${endpoint.name}")
            return true
        } catch (e: Exception) {
            appLogger.e(TAG, "Error during API request validation: ${e.message}")
            return false
        }
    }

}