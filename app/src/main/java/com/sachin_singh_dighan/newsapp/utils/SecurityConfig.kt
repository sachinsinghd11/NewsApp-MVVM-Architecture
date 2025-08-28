package com.sachin_singh_dighan.newsapp.utils

import android.Manifest
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.sachin_singh_dighan.newsapp.BuildConfig
import com.sachin_singh_dighan.newsapp.security.BiometricManager
import com.sachin_singh_dighan.newsapp.security.KeystoreManager
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecurityConfig @Inject constructor(
    private val context: Context,
    private val keystoreManager: KeystoreManager,
    private val biometricManager: BiometricManager,
    private val appLogger: Logger
) {

    companion object {
        private const val TAG = "SecurityConfig"
    }

    /**
     * Initialize security configuration
     */
    fun initializeSecurity(): Boolean {
        return try {
            appLogger.d(TAG, "Initializing security configuration")

            // Check if Keystore is available
            if (!keystoreManager.isKeystoreAvailable()) {
                appLogger.e(TAG, "Android Keystore is not available")
                return BuildConfig.DEBUG
            }

            // Check if API key exists, if not, store it
            if (!keystoreManager.hasApiKey()) {
                appLogger.d(TAG, "No API key found, storing initial API key")

                // Get API key from BuildConfig (for debug builds)
                val apiKey = getInitialApiKey()
                if (apiKey.isNotEmpty()) {
                    if (!keystoreManager.storeApiKey(apiKey)) {
                        appLogger.e(TAG, "Failed to store initial API key")
                        return BuildConfig.DEBUG // don’t hard-fail in debug
                    }
                } else {
                    appLogger.e(TAG, "No initial API key available")
                    return BuildConfig.DEBUG // allow app to run in debug
                }
            }

            appLogger.d(TAG, "Security configuration initialized successfully")
            true

        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to initialize security configuration: ${e.message}")
            false
        }
    }

    /**
     * Get initial API key from BuildConfig or other sources
     */
    private fun getInitialApiKey(): String {
        // Dev: use BuildConfig value if present; Prod: prefer keystore/runtime (no hardcoded fallback)
        return BuildConfig.NEWS_API_KEY.takeIf { it.isNotBlank() } ?: ""
    }

    /**
     * Get security status information
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresPermission(Manifest.permission.USE_BIOMETRIC)
    fun getSecurityStatus(): String {
        return """
            Security Status:
            - Keystore Available: ${keystoreManager.isKeystoreAvailable()}
            - API Key Stored: ${keystoreManager.hasApiKey()}
            - Biometric Available: ${biometricManager.isBiometricAvailable()}
            - Biometric Status: ${biometricManager.getBiometricStatus()}
            - Keystore Info: ${keystoreManager.getKeystoreInfo()}
        """.trimIndent()
    }

    /**
     * Validate security configuration
     */
    fun validateSecurity(): Boolean {
        return keystoreManager.isKeystoreAvailable() &&
                keystoreManager.hasApiKey() &&
                keystoreManager.getApiKey()?.isNotEmpty() == true
    }
}