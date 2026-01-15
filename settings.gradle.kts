@file:Suppress("UnstableApiUsage")

rootProject.name = "Katana"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

plugins {
    id("com.gradle.develocity") version "4.3.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

develocity {
    val isCI = !System.getenv("CI").isNullOrEmpty()

    buildScan {
        termsOfUseUrl = "https://gradle.com/terms-of-service"
        termsOfUseAgree = "yes"
        publishing.onlyIf { isCI }
    }
}

// Keep in sync with build-logic/settings.gradle.kts
buildCache {
    local {
        directory = rootDir.resolve(".gradle/build-cache")
    }
}

include(":app-android", ":shared")
includes("common", "core", "features")

fun includes(vararg directories: String, maxDepth: Int = 2) {
    directories.forEach { topDir ->
        rootDir.resolve(topDir)
            .walkTopDown()
            .maxDepth(maxDepth)
            .filter { file ->
                file.isDirectory && file.resolve("build.gradle.kts").exists()
            }.forEach { module ->
                include(":${module.relativeTo(rootDir).path.replace(File.separatorChar, ':')}")
            }
    }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
