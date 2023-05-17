plugins {
    id("classifi.android.feature")
    id("classifi.android.library.compose")
}

android {
    namespace = "com.khalidtouch.classifiadmin.settings"
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
}