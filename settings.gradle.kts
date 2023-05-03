pluginManagement {
    includeBuild("buildlogic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "ClassifiAdmin"
include (":app")
include (":catalog")
include (":core")
include (":core:common")
include (":core:designsystem")
include (":feature")
include (":feature:feeds")
include (":feature:students")
include (":feature:assessment")
include (":feature:reports")
include (":feature:settings")
include (":core:model")
