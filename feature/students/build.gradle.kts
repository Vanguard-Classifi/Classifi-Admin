plugins {
    id("classifi.android.feature")
    id("classifi.android.library.compose")
}

android {
    namespace  = "com.khalidtouch.classifiadmin.students"
}

dependencies {
    implementation(libs.androidx.activity.compose)
}