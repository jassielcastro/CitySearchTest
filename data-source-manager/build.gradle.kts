plugins {
    id("com.android.library")
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
}

android {
    namespace = "com.ajcm.data_source_manager"
    compileSdk = 35

    defaultConfig {
        minSdk = 26
    }

    buildTypes {
        release {
            buildConfigField("Boolean", "DEBUG", "false")
        }

        debug {
            buildConfigField("Boolean", "DEBUG", "true")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(project(":storage"))
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.moshi)
    implementation(libs.retrofit.converter.moshi.adapter)
    implementation(libs.retrofit.okhttp)
    implementation(libs.retrofit.interceptor)
    implementation(libs.koin.core)
    implementation(libs.moshi)
    ksp(libs.moshi.code.gen)
    implementation(libs.room.paging)

    testImplementation(libs.junit)
    testImplementation(libs.room.testing)
    testImplementation(libs.google.truth)
    testImplementation(libs.kotlinx.coroutines)
    testImplementation(libs.mockK)
}
