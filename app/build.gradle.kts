import org.jetbrains.kotlin.gradle.utils.toSetOrEmpty

plugins {
    id("classifi.android.application")
    id("classifi.android.application.compose")
    id("classifi.android.application.flavors")
    id("classifi.android.hilt")
    id("classifi.android.application.firebase")
}

android {
    namespace = "com.vanguard.classifiadmin"

    defaultConfig {
        applicationId = "com.vanguard.classifiadmin"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }


    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    val composeBom = platform(libs.androidx.compose.bom)
    val firebaseBom = platform(libs.firebase.bom)

    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
    implementation(project(":core:common"))
    implementation(project(":feature:feeds"))
    implementation(project(":feature:students"))
    implementation(project(":feature:assessment"))
    implementation(project(":feature:reports"))
    implementation(project(":feature:settings"))

    implementation(composeBom)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.androidx.test.espresso)


    androidTestImplementation(composeBom)
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material.icons.ext)
    implementation(libs.androidx.compose.animation) //
    implementation(libs.accompanist.systemuicontroller)//----
    implementation(libs.androidx.appcompat)//------
    implementation(libs.androidx.lifecycle.extensions)//---
    implementation(libs.accompanist.pager)//----
    implementation(libs.accompanist.pager.indicators)//----
    implementation(libs.android.material)//----
    implementation(libs.androidx.constraintlayout)//-----
    implementation(libs.androidx.constraintlayout.compose)//-----
    implementation(libs.androidx.navigation.runtime)//---
    implementation(libs.androidx.navigation.compose)//---
    implementation(libs.androidx.datastore.preferences)//---
    implementation(libs.androidx.lifecycle.viewmodel)//---
    implementation(libs.androidx.lifecycle.livedata)//---
    implementation(libs.androidx.lifecycle.java8)//---
    implementation(libs.play.services.auth)//----
    implementation(libs.accompanist.navigation.animation)//----
    implementation(libs.androidx.work.runtime)//---
    implementation(libs.androidx.splashscreen)//---
    implementation(libs.androidx.lifecycle.runtime.compose)//----
    implementation(libs.androidx.paging.compose)//---
    implementation(libs.androidx.test.junit.ext)//--
    implementation(libs.androidx.room.runtime)//--
    implementation(libs.androidx.compose.material3.windowSizeClass)
    implementation(libs.androidx.tracing)//---
    testImplementation(libs.androidx.room.testing)//---
    implementation(libs.androidx.room.ktx)//--
    kapt(libs.androidx.room.compiler)//---
    androidTestImplementation(libs.app.cash.turbine)//----
    implementation(libs.google.gson)//---
    implementation(libs.androidx.hilt.navigation.compose)//--

//    implementation(libs.android.tools.build.gradle)
//    implementation(libs.android.tools.build.gradle.api)

//    implementation(libs.firebase.performance)
//    implementation(libs.firebase.crashlytics)
}


// Allow references to generated code
kapt {
    correctErrorTypes = true
}