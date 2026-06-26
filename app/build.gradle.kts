import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.room)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "ncshack.samba.mizan"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "ncshack.samba.mizan"
        minSdk = 29
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    buildFeatures {
        compose = true
    }
}


buildscript {
    dependencies {
        classpath(libs.symbol.processing.gradle.plugin)
    }
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_21)
        languageVersion.set(KotlinVersion.KOTLIN_2_2)
        apiVersion.set(KotlinVersion.KOTLIN_2_2)
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.material.kolor)
    implementation(libs.kmpalette.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material.motion.compose.navigation)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.coil.compose)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.compose.viewmodel)
    implementation(libs.koin.compose.viewmodel.navigation)
    implementation(libs.koin.androidx.workmanager)
    implementation(libs.androidx.collection)
    implementation(libs.androidx.collection.ktx)
    implementation(libs.disk.lru.cache)
    implementation(libs.bundles.ktor)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.androidx.animation.graphics)
    implementation(libs.compose.dnd)
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.work.runtime.ktx)
}