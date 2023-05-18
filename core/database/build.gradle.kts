plugins {
    id("classifi.android.library")
    id("classifi.android.hilt")
    id("classifi.android.room")
    id("kotlinx-serialization")
}

android {
    namespace = "com.khalidtouch.chatme.database"
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(project(":core:model"))
}