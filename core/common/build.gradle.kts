plugins {
    id("classifi.android.library")
    id("classifi.android.hilt")
}

android {
    namespace = "com.khalidtouch.core.common"
}

dependencies {
    implementation(libs.kotlinx.coroutines.android)
}