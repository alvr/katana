rootProject.name = "Katana"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

include(
    ":app",

    ":data:preferences:base",
    ":data:preferences:token",

    ":data:remote:base",

    ":domain:base",
    ":domain:token",

    ":ui:base",
    ":ui:login",
)

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
