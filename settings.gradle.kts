@file:Suppress("UnstableApiUsage")

rootProject.name = "Katana"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.develocity") version "3.18"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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
includes("common", "core", "features", maxDepth = 2)

fun includes(vararg directories: String, maxDepth: Int = 1) {
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
