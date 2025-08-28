import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
    id("dagger.hilt.android.plugin")
    id("org.jetbrains.kotlin.plugin.compose")
}

val versionCodeProp = providers.gradleProperty("VERSION_CODE").orNull?.toInt() ?: 1
val versionNameProp = providers.gradleProperty("VERSION_NAME").orNull ?: "1.0.0"

// ---- Secrets (prefer ENV, then ~/.gradle/gradle.properties) ----
// ---- Secrets (prefer ENV, then ~/.gradle/gradle.properties) ----
val newsApiKeyProvider = providers.environmentVariable("NEWS_API_KEY")
    .orElse(providers.gradleProperty("NEWS_API_KEY"))

if (!newsApiKeyProvider.isPresent) {
    logger.warn("⚠️ NEWS_API_KEY not set via ENV or gradle.properties; BuildConfig will contain an empty value.")
}

// Resolve once, at configuration time, to a plain String
val newsApiKey: String =
    providers.environmentVariable("NEWS_API_KEY")
        .orElse(providers.gradleProperty("NEWS_API_KEY"))
        .orNull
        ?: ""   // fallback for local dev

android {
    namespace = "com.sachin_singh_dighan.newsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.sachin_singh_dighan.newsapp"
        minSdk = 24
        //noinspection OldTargetApi
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        // BuildConfig fields (NEWS_API_KEY is injected securely)
        // NOTE: buildConfigField wants a Java literal, so we wrap in quotes
        buildConfigField("String", "NEWS_API_KEY", "\"$newsApiKey\"")
        // BASE_URL now comes from flavors below
    }

    // ---- Secure release signing (env/gradle.properties; never hardcode) ----
    signingConfigs {
        create("release") {
            val storePath = providers.environmentVariable("ANDROID_KEYSTORE_PATH").orNull
                ?: providers.gradleProperty("RELEASE_STORE_FILE").orNull
            val storePass = providers.environmentVariable("ANDROID_KEYSTORE_PASSWORD").orNull
                ?: providers.gradleProperty("RELEASE_STORE_PASSWORD").orNull
            val keyAlias = providers.environmentVariable("ANDROID_KEY_ALIAS").orNull
                ?: providers.gradleProperty("RELEASE_KEY_ALIAS").orNull
            val keyPass = providers.environmentVariable("ANDROID_KEY_PASSWORD").orNull
                ?: providers.gradleProperty("RELEASE_KEY_PASSWORD").orNull

            if (storePath != null && storePass != null && keyAlias != null && keyPass != null) {
                storeFile = file(storePath)
                storePassword = storePass
                this.keyAlias = keyAlias
                keyPassword = keyPass
            } else {
                println("⚠️ Release signing not configured; using debug keystore for local builds.")
                //useDebugSigning()
            }
        }
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "true")
            buildConfigField("String", "ENV", "\"dev\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Make sure you’ve set signing via env/gradle.properties (see notes)
            signingConfig = signingConfigs.getByName("release"){
                if (newsApiKey.isBlank()) {
                    throw GradleException("NEWS_API_KEY is missing for release build. Set ENV or ~/.gradle/gradle.properties")
                }
            }
            buildConfigField("boolean", "IS_LOGGING_ENABLED", "false")
            buildConfigField("String", "ENV", "\"prod\"")
        }
    }

    // --- Environments (dev/staging/prod) ---
    flavorDimensions += "env"
    productFlavors {
        create("dev") {
            dimension = "env"
            applicationIdSuffix = ".dev"
            versionNameSuffix = "-dev"
            resValue("string", "app_name", "NewsApp Dev")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
        }
        create("staging") {
            dimension = "env"
            applicationIdSuffix = ".staging"
            versionNameSuffix = "-staging"
            resValue("string", "app_name", "NewsApp Staging")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
        }
        create("prod") {
            dimension = "env"
            // no suffix; this is your Play build
            resValue("string", "app_name", "NewsApp")
            buildConfigField("String", "BASE_URL", "\"https://newsapi.org/v2/\"")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        jvmToolchain(17)
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
        viewBinding  = true
        compose = true
    }
    buildToolsVersion = "35.0.0"

    // composeOptions { kotlinCompilerExtensionVersion is managed by BOM or AGP; omit if using latest AGP }

    // Lint should fail CI
    lint {
        checkReleaseBuilds = true
        abortOnError = true
        warningsAsErrors = true
        xmlReport = true
        htmlReport = true
        baseline = file("lint-baseline.xml") // generate once then curate
    }

    // Tests
    testOptions {
        animationsDisabled = true
        unitTests {
            isReturnDefaultValues = true
            isIncludeAndroidResources = true
        }
    }

    // Packaging hygiene
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1,LICENSE,LICENSE.txt,NOTICE}"
            excludes += "/META-INF/*.kotlin_module"
        }
    }

    // App Bundle optimizations
    bundle {
        language { enableSplit = true }
    }

    // (Optional) restrict shipped locales
    // defaultConfig { resourceConfigurations += listOf("en") }
}


dependencies {

    implementation("androidx.core:core-ktx:1.17.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.1")
    implementation("androidx.recyclerview:recyclerview:1.4.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    //implementation("android.arch.lifecycle:extensions:1.1.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.9.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.9.2")
    implementation("com.google.dagger:hilt-android:2.57.1")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    ksp("com.google.dagger:hilt-compiler:2.57.1")
    //implementation("org.glassfish:javax.annotation:10.0-b28")
    implementation("androidx.browser:browser:1.9.0")

    val composeBom = platform("androidx.compose:compose-bom:2025.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-tooling-preview")
    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.activity:activity-compose:1.10.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.9.2")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Hilt WorkManager
    implementation("androidx.hilt:hilt-work:1.2.0")
    ksp("androidx.hilt:hilt-compiler:1.2.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.10.3")

    //Navigation
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.navigation:navigation-compose:2.9.3")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.9.2")

    //Paging
    implementation("androidx.paging:paging-runtime-ktx:3.3.6")
    implementation("androidx.paging:paging-compose:3.3.6")

    //Room
    implementation("androidx.room:room-runtime:2.7.2")
    implementation("androidx.room:room-ktx:2.7.2")
    ksp("androidx.room:room-compiler:2.7.2")

    // Desugaring for Java 8+ APIs on old devices
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.1.5")
    // UI Tests
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    androidTestImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.19.0")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation("app.cash.turbine:turbine:1.2.1")
    androidTestImplementation("androidx.test.ext:junit:1.3.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.7.0")
    androidTestImplementation("androidx.navigation:navigation-testing:2.9.3")
    testImplementation(kotlin("test"))
}