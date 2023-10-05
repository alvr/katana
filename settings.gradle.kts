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
        mavenCentral()
        google()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}

plugins {
    id("com.gradle.enterprise") version "3.15.1"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.7.0"
}

// Keep in sync with build-logic/settings.gradle.kts
buildCache {
    local {
        directory = rootDir.resolve(".gradle/build-cache")
    }
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

include(":androidApp")

// Include all modules in these directories
listOf("common", "data/preferences", "data/remote", "domain", "ui").forEach { topDir ->
    rootDir.resolve(topDir)
        .walkTopDown()
        .maxDepth(1)
        .filter { file ->
            file.isDirectory && file.resolve("build.gradle.kts").exists()
        }.forEach { module ->
            include(":${topDir.replace('/', ':')}:${module.name}")
        }
}

enableFeaturePreview("STABLE_CONFIGURATION_CACHE")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
