plugins {
    id("classifi.android.feature")
    id("classifi.android.library.compose")
}

android {
    namespace = "com.khalidtouch.classifiadmin.settings"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}