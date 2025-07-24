# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# ===== ENCRYPTED STORAGE SECURITY =====
# Keep encrypted storage classes
-keep class com.sachin_singh_dighan.newsapp.utils.EncryptedApiKeyManager { *; }
-keep class androidx.security.crypto.** { *; }

#=========Encrypted Sotrage Security=========
-keep class com.sachin_singh_dighan.newsapp.utils.EncryptedApiKeyManager{ *; }
-keep class androidx.security.crypto.**{ *; }

# ===== NETWORK SECURITY =====
# Keep Retrofit and OkHttp classes
-keepattributes Signature
-keepattributes *Annotation*
-keep class retrofit2.** { *; }
-keepclasseswithmembers class * {
    @retrofit2.http.* <methods>;
}
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}


# OkHttp Security
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-dontwarn org.conscrypt.**

# ===== DATABASE RULES =====
# Room Database
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-dontwarn androidx.room.paging.**

# ===== DEPENDENCY INJECTION =====
# Hilt
-keep,allowobfuscation @dagger.hilt.android.lifecycle.HiltViewModel class * extends androidx.lifecycle.ViewModel

# ===== COMPOSE RULES =====
# Keep Compose related classes
-keep class androidx.compose.** { *; }
-keepclassmembers class androidx.compose.** { *; }

# ===== DEBUGGING =====
# Preserve line number information for debugging stack traces
-keepattributes SourceFile,LineNumberTable

# Hide the original source file name
-renamesourcefileattribute SourceFile

# ===== WEBVIEW (if needed) =====
# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}