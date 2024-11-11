import com.android.build.api.dsl.Packaging
import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.google.ksp)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
}

val keystorePropertiesFile: File = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

val appConfigPropertiesFile: File = rootProject.file("appconfig.properties")
val appConfigProperties = Properties()
appConfigProperties.load(FileInputStream(appConfigPropertiesFile))

fun getKeystoreProperty(variant: String, key: String): String? {
    return keystoreProperties["$variant.$key"]?.toString()
}

fun getAppConfigProperty(variant: String, key: String): String? {
    return appConfigProperties["$variant.$key"]?.toString()
}

android {
    namespace = "com.nielaclag.openweather"
    compileSdk = 35

    signingConfigs {
        create("release") {
            val variant = "release"
            keyAlias = getKeystoreProperty(variant, "keyAlias")
            keyPassword = getKeystoreProperty(variant, "keyPassword")
            storeFile = getKeystoreProperty(variant, "storeFile")?.let { filePath -> file(filePath) }
            storePassword = getKeystoreProperty(variant, "storePassword")
        }
        create("development") {
            val variant = "development"
            keyAlias = getKeystoreProperty(variant, "keyAlias")
            keyPassword = getKeystoreProperty(variant, "keyPassword")
            storeFile = getKeystoreProperty(variant, "storeFile")?.let { filePath -> file(filePath) }
            storePassword = getKeystoreProperty(variant, "storePassword")
        }
        getByName("debug") {
            val variant = "debug"
            keyAlias = getKeystoreProperty(variant, "keyAlias")
            keyPassword = getKeystoreProperty(variant, "keyPassword")
            storeFile = getKeystoreProperty(variant, "storeFile")?.let { filePath -> file(filePath) }
            storePassword = getKeystoreProperty(variant, "storePassword")
        }
    }

    defaultConfig {
        applicationId = "com.nielaclag.openweather"
        minSdk = 26
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.nielaclag.openweather.util.CustomTestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
            buildConfigField("String", "BUILD_VARIANT", "\"release\"")
            buildConfigField("String", "GOOGLE_OAUTH_SERVER_CLIENT_ID", "\"${getAppConfigProperty("release", "googleOauthServerClientId")}\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${getAppConfigProperty("release", "openWeatherApiKey")}\"")
        }
        debug {
            isShrinkResources = false
            isMinifyEnabled = false
            isDebuggable = true

            versionNameSuffix = "-debug"
            applicationIdSuffix = ".debug"
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
            buildConfigField("String", "BUILD_VARIANT", "\"debug\"")
            buildConfigField("String", "GOOGLE_OAUTH_SERVER_CLIENT_ID", "\"${getAppConfigProperty("debug", "googleOauthServerClientId")}\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${getAppConfigProperty("debug", "openWeatherApiKey")}\"")
        }
        create("development") {
            isShrinkResources = true
            isMinifyEnabled = true
            isDebuggable = false

            versionNameSuffix = "-development"
            applicationIdSuffix = ".development"

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("development")
            buildConfigField("String", "BUILD_VARIANT", "\"development\"")
            buildConfigField("String", "GOOGLE_OAUTH_SERVER_CLIENT_ID", "\"${getAppConfigProperty("development", "googleOauthServerClientId")}\"")
            buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${getAppConfigProperty("development", "openWeatherApiKey")}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.kotlin.get()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }
}

dependencies {
    // TEST
    testImplementation(libs.test.jUnit)
    testImplementation(libs.test.androidx.core)
    testImplementation(libs.test.androidx.core.ktx)
    testImplementation(libs.test.androidx.jUnit)
    testImplementation(libs.test.androidx.jUnit.ktx)
    testImplementation(libs.test.androidx.espresso.idlingResource)
    testImplementation(libs.test.androidx.arch.core)
    testImplementation(libs.test.google.truth)
    testImplementation(libs.test.coroutines.test)
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.mockito.inline)
    testImplementation(libs.test.mockk.android)
    testImplementation(libs.test.mockk.agent)

    testImplementation(libs.test.turbine)
    testImplementation(libs.test.robolectric)
    testImplementation(libs.test.hilt)
    testImplementation(libs.test.room)
    kspTest(libs.hilt.compiler)

    // ANDROID TEST
    androidTestImplementation(libs.test.jUnit)
    androidTestImplementation(libs.test.androidx.core)
    androidTestImplementation(libs.test.androidx.core.ktx)
    androidTestImplementation(libs.test.androidx.runner)
    androidTestImplementation(libs.test.androidx.rules)
    androidTestImplementation(libs.test.androidx.jUnit)
    androidTestImplementation(libs.test.androidx.jUnit.ktx)
    androidTestImplementation(libs.test.androidx.espresso.core)
    androidTestImplementation(libs.test.okhttp3.idlingResource)
    androidTestImplementation(libs.test.androidx.arch.core)
    androidTestImplementation(libs.test.google.truth)
    androidTestImplementation(libs.test.mockito.android)
    androidTestImplementation(libs.test.mockk.android)
    androidTestImplementation(libs.test.mockk.agent)

    androidTestImplementation(libs.test.turbine)
    androidTestImplementation(libs.test.mockWebServer)
    androidTestImplementation(libs.test.robolectric)
    androidTestImplementation(libs.test.hilt)
    kspAndroidTest(libs.hilt.compiler)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.test.compose)

    // TIMBER
    implementation(libs.timber)

    // KOTLIN STDLIB
    implementation(libs.kotlin.stdlib)

    // FIREBASE
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.auth)

    // GOOGLE AUTH
    implementation(libs.google.auth)
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.playServices)
    implementation(libs.google.id)

    // GOOGLE LOCATION
    implementation(libs.google.services.location)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.exifinterface)
    implementation(libs.androidx.startup)

    // COMPOSE ACTIVITY
    implementation(libs.androidx.activity)

    // COMPOSE CONSTRAINT LAYOUT
    implementation(libs.androidx.constraintLayout)

    // LIFECYCLE RUNTIME
    implementation(libs.androidx.lifecycle)

    // COMPOSE NAVIGATION INTEGRATION
    implementation(libs.androidx.navigation)

    // SPLASH SCREEN
    implementation(libs.androidx.splashScreen)

    // SAVE STATE
    implementation(libs.androidx.savedstate)

    // WORK MANAGER
    implementation(libs.androidx.workManager)
    implementation(libs.androidx.workManager.multiprocess)

    // SERIALIZATION
    implementation(libs.kotlinx.serialization.json)

    // HILT
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.navigation)

    // WORK MANAGER WITH HILT
    implementation(libs.hilt.worker)
    ksp(libs.hilt.worker.compiler)

    // Material Design (FOR THEME)
    implementation(libs.material)

    //// JETPACK COMPOSE BOM {
    implementation(platform(libs.compose.bom))

    // Material Design 3
    implementation(libs.compose.material3)
    implementation(libs.compose.material3.windowSize)

    implementation(libs.compose.ui)
    implementation(libs.compose.googleFonts)
    implementation(libs.compose.ui.util)

    // Tooling support (Previews, etc.)
    implementation(libs.compose.ui.tooling.preview)
    debugImplementation(libs.compose.ui.tooling)

    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation(libs.compose.foundation)

    // Add full set of material icons
    implementation(libs.compose.material.icons)

    // Add full set of material icons
    implementation(libs.compose.animation)
    //// }

    // COIL
    implementation(libs.coil.compose)
    implementation(libs.coil.svg)
    implementation(libs.coil.gif)
    implementation(libs.coil.video)

    // LOTTIE
    implementation(libs.lottie.compose)

    // PAGING 3
    implementation(libs.paging3.runtime)
    implementation(libs.paging3.compose)

    //// ROOM DATABASE {
    implementation(libs.room.database)
    annotationProcessor(libs.room.compiler)
    // To use Kotlin Symbol Processing (KSP)
    ksp(libs.room.compiler)
    // optional - Kotlin Extensions and Coroutines support for Room
    implementation(libs.room.extensions)
    implementation(libs.room.paging)
    //// }

    // ACCOMPANIST
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.systemUiController)

    // RETROFIT
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)

    // OKHTTP
    implementation(platform(libs.okhttp3.bom))
    implementation(libs.okhttp3.okhttp)
    implementation(libs.okhttp3.logging)

    // MOSHI
    implementation(libs.moshi)
    ksp(libs.moshi.codegen)

    // THREE TEN ABP
    implementation(libs.treeTenAbp)

}