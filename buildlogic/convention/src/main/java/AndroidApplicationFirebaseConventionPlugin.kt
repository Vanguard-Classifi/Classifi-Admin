import com.android.build.api.variant.ApplicationAndroidComponentsExtension
import com.google.firebase.crashlytics.buildtools.gradle.CrashlyticsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationFirebaseConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.google.gms.google-services")
              //  apply("com.google.firebase.firebase-perf")
             //   apply("com.google.firebase.crashlytics")
            }

            val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
            dependencies {
                val bom = libs.findLibrary("firebase.bom").get()
                add("implementation", platform(bom))
//                add("implementation", (libs.findLibrary("firebase.analytics").get()))
//                add("implementation", (libs.findLibrary("firebase.performance").get()))
//                add("implementation", (libs.findLibrary("firebase.crashlytics").get()))
                add("implementation", (libs.findLibrary("firebase.auth").get()))
                add("implementation", (libs.findLibrary("firebase.firestore").get()))
                add("implementation", (libs.findLibrary("firebase.storage").get()))
            }

            extensions.configure<ApplicationAndroidComponentsExtension> {
                finalizeDsl {
                    it.buildTypes.forEach { buildType ->
//                        buildType.configure<CrashlyticsExtension> {
//                            mappingFileUploadEnabled = false
//                        }
                    }
                }
            }
        }

    }
}