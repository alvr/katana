rootProject.name = "build-logic"

enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

include(":common")
include(":analysis-convention")
include(":android-convention")
include(":gradle-convention")
include(":kotlin-convention")
