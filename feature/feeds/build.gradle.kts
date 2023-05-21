plugins {
    id("classifi.android.feature")
    id("classifi.android.library.compose")
}

android {
    namespace = "com.khalidtouch.classifiadmin.feeds"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}