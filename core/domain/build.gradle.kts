plugins {
    id("classifi.android.library")
    id("classifi.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.khalidtouch.chatme.domain"
    compileSdk = 33

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    lint {
        checkDependencies = true
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}