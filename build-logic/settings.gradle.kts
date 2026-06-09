@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

pluginManagement {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent { snapshotsOnly() }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent { snapshotsOnly() }
        }
    }
    versionCatalogs { register("libs") { from(files("../gradle/libs.versions.toml")) } }
}

// Keep in sync with ../settings.gradle.kts
buildCache { local { directory = rootDir.parentFile.resolve(".gradle/build-cache") } }

include(":katana-convention")
