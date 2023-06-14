plugins {
    id("classifi.android.library")
    id("classifi.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.khalidtouch.chatme.network"
    buildFeatures {
        buildConfig = true
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(project(":core:model"))
}