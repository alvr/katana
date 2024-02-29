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
    id("com.gradle.enterprise") version "3.16.2"
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
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

include(":app")

include(":features:account:data")
include(":features:account:domain")
include(":features:account:ui")

include(":features:explore:data")
include(":features:explore:domain")
include(":features:explore:ui")

include(":features:lists:data")
include(":features:lists:domain")
include(":features:lists:ui")

include(":features:login:ui")

include(":features:social:data")
include(":features:social:domain")
include(":features:social:ui")

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
