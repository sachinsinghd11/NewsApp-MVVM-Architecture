package com.sachin_singh_dighan.newsapp.security

import android.content.Context
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.core.content.edit
import com.sachin_singh_dighan.newsapp.utils.logger.Logger
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec
import javax.inject.Inject
import javax.inject.Singleton


private data class EncryptionResult(
    val cipherTextB64: String,
    val ivB64: String
)

@Singleton
class KeystoreManager @Inject constructor(
    private val context: Context,
    private val appLogger: Logger
) {

    companion object {
        private const val TAG = "KeystoreManager"
        private const val KEYSTORE_PROVIDER = "AndroidKeyStore"
        private const val KEY_ALIAS = "NewsAppApiKey"
        private const val TRANSFORMATION = "AES/GCM/NoPadding"
        private const val KEY_SIZE = 256
        private const val GCM_IV_LENGTH = 12
        private const val GCM_TAG_LENGTH = 128
    }

    private val keyStore: KeyStore by lazy {
        KeyStore.getInstance(KEYSTORE_PROVIDER).apply {
            load(null)
        }
    }

    /**
     * Store API key securely in Android Keystore
     */
    fun storeApiKey(apiKey: String): Boolean {
        return try {
            appLogger.d(TAG, "Storing API key in Android Keystore")

            if (!isValidApiKey(apiKey)) {
                appLogger.e(TAG, "Invalid API key provided for storage")
                return false
            }

            // Use existing key if present, otherwise generate one
            val secretKey = if (keyStore.containsAlias(KEY_ALIAS)) {
                getSecretKey()
            } else {
                generateSecretKey()
            }

            // Let Cipher generate a fresh random IV (do NOT supply your own)
            val enc = encryptApiKey(apiKey, secretKey)

            // Persist ciphertext + IV
            val sharedPrefs = context.getSharedPreferences("SecureStorage", Context.MODE_PRIVATE)
            sharedPrefs.edit {
                putString("encrypted_api_key", enc.cipherTextB64)
                putString("iv", enc.ivB64)
            }

            appLogger.d(TAG, "API key stored successfully in Android Keystore")
            true
        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to store API key: ${e.message}")
            false
        }
    }

    /**
     * Validate API key format and length
     */
    private fun isValidApiKey(apiKey: String): Boolean {
        return try {
            // Check if API key is not null or empty
            if (apiKey.isBlank()) {
                appLogger.e(TAG, "API key is blank")
                return false
            }

            // Check minimum length (NewsAPI keys are typically 32 characters)
            if (apiKey.length < 10) {
                appLogger.e(TAG, "API key too short: ${apiKey.length} chars")
                return false
            }

            // Check if it contains only valid characters (alphanumeric and hyphens)
            if (!apiKey.matches(Regex("^[a-zA-Z0-9-]+$"))) {
                appLogger.e(TAG, "API key contains invalid characters")
                return false
            }

            appLogger.d(TAG, "API key validation passed: ${apiKey.length} chars")
            true

        } catch (e: Exception) {
            appLogger.e(TAG, "Error during API key validation: ${e.message}")
            false
        }
    }

    /**
     * Retrieve API key from Android Keystore
     */
    fun getApiKey(): String? {
        return try {
            appLogger.d(TAG, "Retrieving API key from Android Keystore")

            // Get encrypted API key from SharedPreferences
            val sharedPrefs = context.getSharedPreferences("SecureStorage", Context.MODE_PRIVATE)
            val encryptedApiKey = sharedPrefs.getString("encrypted_api_key", null)
            val ivString = sharedPrefs.getString("iv", null)

            if (encryptedApiKey == null || ivString == null) {
                appLogger.w(TAG, "No encrypted API key found in storage")
                return null
            }

            // Get secret key from Keystore
            val secretKey = getSecretKey()

            // Decrypt API key
            val iv = android.util.Base64.decode(ivString, android.util.Base64.NO_WRAP)
            val decryptedApiKey = decryptApiKey(encryptedApiKey, secretKey, iv)

            appLogger.d(TAG, "API key retrieved successfully from Android Keystore")
            decryptedApiKey

        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to retrieve API key: ${e.message}")
            null
        }
    }

    /**
     * Check if API key exists in Keystore
     */
    fun hasApiKey(): Boolean {
        return try {
            val sharedPrefs = context.getSharedPreferences("SecureStorage", Context.MODE_PRIVATE)
            val encryptedApiKey = sharedPrefs.getString("encrypted_api_key", null)
            encryptedApiKey != null
        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to check API key existence: ${e.message}")
            false
        }
    }

    /**
     * Clear API key from Keystore
     */
    fun clearApiKey(): Boolean {
        return try {
            appLogger.d(TAG, "Clearing API key from Android Keystore")

            // Clear from SharedPreferences
            val sharedPrefs = context.getSharedPreferences("SecureStorage", Context.MODE_PRIVATE)
            sharedPrefs.edit { clear() }

            // Remove key from Keystore
            if (keyStore.containsAlias(KEY_ALIAS)) {
                keyStore.deleteEntry(KEY_ALIAS)
            }

            appLogger.d(TAG, "API key cleared successfully from Android Keystore")
            true

        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to clear API key: ${e.message}")
            false
        }
    }

    /**
     * Generate secret key for encryption
     */
    private fun generateSecretKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            KEYSTORE_PROVIDER
        )

        val keyGenParameterSpec = KeyGenParameterSpec.Builder(
            KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(KEY_SIZE)
            .setUserAuthenticationRequired(false)
            .setRandomizedEncryptionRequired(true)
            .build()

        keyGenerator.init(keyGenParameterSpec)
        return keyGenerator.generateKey()
    }

    /**
     * Get existing secret key from Keystore
     */
    private fun getSecretKey(): SecretKey {
        return try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                appLogger.e(TAG, "Secret key alias '$KEY_ALIAS' not found in Keystore")
                throw IllegalStateException("Secret key not found in Keystore")
            }
            
            val keyEntry = keyStore.getEntry(KEY_ALIAS, null) as KeyStore.SecretKeyEntry
            appLogger.d(TAG, "Secret key retrieved successfully from Keystore")
            keyEntry.secretKey
            
        } catch (e: Exception) {
            appLogger.e(TAG, "Failed to get secret key from Keystore: ${e.message}")
            throw e
        }
    }

    /**
     * Encrypt API key using AES/GCM
     */
    private fun encryptApiKey(apiKey: String, secretKey: SecretKey): EncryptionResult {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        // ✅ Don't pass IV; Keystore will generate a random IV
        cipher.init(Cipher.ENCRYPT_MODE, secretKey)

        val encryptedBytes = cipher.doFinal(apiKey.toByteArray())
        val iv = cipher.iv // ← Use the IV generated by the cipher

        return EncryptionResult(
            cipherTextB64 = android.util.Base64.encodeToString(encryptedBytes, android.util.Base64.NO_WRAP),
            ivB64 = android.util.Base64.encodeToString(iv, android.util.Base64.NO_WRAP)
        )
    }

    /**
     * Decrypt API key using AES/GCM
     */
    private fun decryptApiKey(encryptedApiKey: String, secretKey: SecretKey, iv: ByteArray): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)
        cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        val encryptedBytes = android.util.Base64.decode(encryptedApiKey, android.util.Base64.NO_WRAP)
        val decryptedBytes = cipher.doFinal(encryptedBytes)
        return String(decryptedBytes)
    }

    /**
     * Check if Keystore is available
     */
    fun isKeystoreAvailable(): Boolean {
        return try {
            keyStore.load(null)
            true
        } catch (e: Exception) {
            appLogger.e(TAG, "Keystore not available: ${e.message}")
            false
        }
    }

    /**
     * Get Keystore information for debugging
     */
    fun getKeystoreInfo(): String {
        return try {
            val aliases = keyStore.aliases()
            val aliasList = mutableListOf<String>()
            while (aliases.hasMoreElements()) {
                aliasList.add(aliases.nextElement())
            }

            """
            Keystore Info:
            - Provider: ${keyStore.provider}
            - Type: ${keyStore.type}
            - Size: ${keyStore.size()}
            - Aliases: ${aliasList.joinToString(", ")}
            - Contains API Key: ${keyStore.containsAlias(KEY_ALIAS)}
            """.trimIndent()
        } catch (e: Exception) {
            "Failed to get Keystore info: ${e.message}"
        }
    }
}