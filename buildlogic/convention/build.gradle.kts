
plugins {
    `kotlin-dsl`
}

group = "com.khalidtouch.classifiadmin.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}


dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.firebase.crashlytics.gradle)
    compileOnly(libs.firebase.performance.gradle)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.android.tools.build.gradle)
    compileOnly(libs.android.tools.build.gradle.api)
}

gradlePlugin {
    plugins {
        register("androidApplicationCompose") {
            id = "classifi.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("androidApplication") {
            id = "classifi.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidLibraryCompose") {
            id = "classifi.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }

        register("androidLibrary") {
            id = "classifi.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        /*todo -> feature */

        register("androidTest"){
            id = "classifi.android.test"
            implementationClass = "AndroidTestConventionPlugin"
        }

        register("androidHilt"){
            id = "classifi.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }

        register("androidFeature"){
            id = "classifi.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidRoom") {
            id = "classifi.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }

        register("androidFirebase"){
            id = "classifi.android.application.firebase"
            implementationClass = "AndroidApplicationFirebaseConventionPlugin"
        }

        register("androidFlavors"){
            id = "classifi.android.application.flavors"
            implementationClass = "AndroidApplicationFlavorsConventionPlugin"
        }
    }
}
