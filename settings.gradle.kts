rootProject.name = "Katana"

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.gradle.enterprise") version "3.11.2"
}

gradleEnterprise {
    buildScan {
        termsOfServiceUrl = "https://gradle.com/terms-of-service"
        termsOfServiceAgree = "yes"
    }
}

include(":app")

// Include all modules in these directories
// TODO https://kotlinlang.org/docs/whatsnew1720.html#standard-library
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
