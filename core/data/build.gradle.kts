plugins {
    id("classifi.android.library")
    id("classifi.android.hilt")
    id("kotlinx-serialization")
}

android {
    namespace = "com.khalidtouch.classifiadmin.data"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}