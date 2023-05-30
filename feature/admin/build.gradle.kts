plugins {
    id("classifi.android.feature")
    id("classifi.android.library.compose")
}

android {
    namespace = "com.khalidtouch.chatme.admin"
   }

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.accompanist.navigation.animation)
    implementation(libs.androidx.compose.animation)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.coil.kt.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.paging.common)
    implementation(libs.androidx.paging.runtime)
    implementation(libs.androidx.paging.compose)
}