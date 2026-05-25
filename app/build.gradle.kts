import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
}

kotlin {
    jvmToolchain(17)
}

android {
    namespace = "il.soulSalttrader.shabbattimes"
    compileSdk = 37

    defaultConfig {
        applicationId = "il.soulSaltrader.shabbattimes"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "v1.5.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        val secretsFile by lazy {
            rootProject.file("secrets.properties")
        }

        val geoApiKey: String by lazy {
            val properties = Properties()

            secretsFile
                .takeIf { it.exists() && it.isFile }
                ?.inputStream()
                ?.use { properties.load(it) }

            properties.getProperty("geoapify.api.key")
                ?: System.getenv("GEOAPIFY_API_KEY")
                ?: error("GEOAPIFY_API_KEY environment variable is required!")
        }

        buildConfigField(
            type = "String",
            name = "GEOAPIFY_API_KEY",
            value = "\"$geoApiKey\""
        )
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            pickFirsts += "/META-INF/versions/9/OSGI-INF/MANIFEST.MF"
        }
    }
}

room { schemaDirectory("$projectDir/schemas") }

dependencies {

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlinx.collections.immutable)

    // UI
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.reorderable)

    // Navigation
    implementation(libs.navigation.compose)
    implementation(libs.kotlinx.serialization.json)

    // Dependency Injection
    implementation(libs.hilt)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Location
    implementation(libs.play.services.location)

    // Persistence
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Unit Testing
    testImplementation(libs.junit4)
    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    testImplementation(libs.hilt.android.testing)

    // UI / Instrumented Testing
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)

    // Debug
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}