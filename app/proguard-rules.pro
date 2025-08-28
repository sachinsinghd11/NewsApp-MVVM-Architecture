# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# ===== SECURITY & KEYSTORE RULES =====

# Keep SecurityConfig and related classes
-keep class com.sachin_singh_dighan.newsapp.utils.SecurityConfig { *; }
-keep class com.sachin_singh_dighan.newsapp.security.** { *; }

# Keep KeystoreManager and related classes
-keep class com.sachin_singh_dighan.newsapp.security.KeystoreManager { *; }
-keep class com.sachin_singh_dighan.newsapp.security.ProductionApiKeyManager { *; }
-keep class com.sachin_singh_dighan.newsapp.security.BiometricManager { *; }

# Keep BuildConfig fields
-keepclassmembers class com.sachin_singh_dighan.newsapp.BuildConfig {
    public static final String NEWS_API_KEY;
    public static final boolean DEBUG;
}

# ===== NETWORK SECURITY RULES =====

# Keep NetworkSecurityService and related classes
-keep class com.sachin_singh_dighan.newsapp.security.NetworkSecurityService { *; }
-keep class com.sachin_singh_dighan.newsapp.security.NetworkSecurityValidator { *; }

# Keep interceptors
-keep class com.sachin_singh_dighan.newsapp.network.interceptor.** { *; }

# ===== API & NETWORK RULES =====

# Keep Retrofit and API related classes
-keep class com.sachin_singh_dighan.newsapp.data.remote.** { *; }
-keep class com.sachin_singh_dighan.newsapp.data.model.** { *; }

# Keep API response models
-keep class com.sachin_singh_dighan.newsapp.data.model.api.** { *; }
-keep class com.sachin_singh_dighan.newsapp.data.model.entity.** { *; }

# ===== DEPENDENCY INJECTION RULES =====

# Keep Dagger Hilt classes
-keep class dagger.hilt.** { *; }
-keep class * extends dagger.hilt.android.internal.managers.ViewComponentManager { *; }

# Keep ApplicationModule and related classes
-keep class com.sachin_singh_dighan.newsapp.di.** { *; }

# ===== ROOM DATABASE RULES =====

# Keep Room database and entities
-keep class com.sachin_singh_dighan.newsapp.data.local.** { *; }
-keep class * extends androidx.room.RoomDatabase { *; }

# ===== WORKMANAGER RULES =====

# Keep WorkManager related classes
-keep class com.sachin_singh_dighan.newsapp.worker.** { *; }
-keep class * extends androidx.work.Worker { *; }

# ===== LOGGING RULES =====

# Keep Logger and logging related classes
-keep class com.sachin_singh_dighan.newsapp.utils.logger.** { *; }

# ===== GENERAL ANDROID RULES =====

# Keep Android components
-keep class * extends android.app.Activity { *; }
-keep class * extends android.app.Application { *; }
-keep class * extends android.app.Service { *; }
-keep class * extends android.content.BroadcastReceiver { *; }
-keep class * extends android.content.ContentProvider { *; }

# Keep ViewModels
-keep class * extends androidx.lifecycle.ViewModel { *; }
-keep class * extends androidx.lifecycle.AndroidViewModel { *; }

# Keep Compose related classes
-keep class androidx.compose.** { *; }
-keep class * implements androidx.compose.runtime.Composable { *; }

# ===== REFLECTION RULES =====

# Allow reflection for BuildConfig access
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes SourceFile,LineNumberTable

# ===== OPTIMIZATION RULES =====

# Don't obfuscate method names for security classes
-keepnames class com.sachin_singh_dighan.newsapp.security.** { *; }
-keepnames class com.sachin_singh_dighan.newsapp.utils.SecurityConfig { *; }

# ===== DEBUGGING RULES =====

# Keep stack traces readable
-keepattributes Exceptions
-keepattributes InnerClasses

# ===== SPECIFIC CLASS RULES =====

# Keep specific classes that might be used in reflection
-keep class com.sachin_singh_dighan.newsapp.MainActivity { *; }
-keep class com.sachin_singh_dighan.newsapp.NewsApplication { *; }

# Keep enum classes
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}