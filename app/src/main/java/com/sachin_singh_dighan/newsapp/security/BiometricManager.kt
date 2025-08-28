package com.sachin_singh_dighan.newsapp.security

import android.Manifest
import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BiometricManager @Inject constructor(
    private val context: Context,
    private val appLogger: Logger
) {

    companion object {
        private const val TAG = "BiometricManager"
    }

    /**
     * Check if biometric authentication is available
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresPermission(Manifest.permission.USE_BIOMETRIC)
    fun isBiometricAvailable(): Boolean {
        val biometricManager = context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> {
                appLogger.d(TAG, "Biometric authentication is available")
                true
            }
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                appLogger.w(TAG, "No biometric hardware available")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                appLogger.w(TAG, "Biometric hardware is unavailable")
                false
            }
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                appLogger.w(TAG, "No biometric enrolled")
                false
            }
            else -> {
                appLogger.w(TAG, "Biometric authentication not available")
                false
            }
        }
    }

    /**
     * Get biometric authentication status
     */
    @RequiresApi(Build.VERSION_CODES.R)
    @RequiresPermission(Manifest.permission.USE_BIOMETRIC)
    fun getBiometricStatus(): String {
        val biometricManager = context.getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager
        return when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> "Available"
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> "No Hardware"
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> "Hardware Unavailable"
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> "Not Enrolled"
            else -> "Unknown"
        }
    }
}