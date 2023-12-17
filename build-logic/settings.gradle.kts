rootProject.name = "build-logic"

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        register("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

// Keep in sync with ../settings.gradle.kts
buildCache {
    local {
        directory = rootDir.parentFile.resolve(".gradle/build-cache")
    }
}

include(":katana-convention")
