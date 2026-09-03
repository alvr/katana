@file:Suppress("UnstableApiUsage")

rootProject.name = "build-logic"

pluginManagement {
    repositories {
        maven {
            url = uri("https://gluoneurope.jfrog.io/artifactory/ods-maven-android-public/")
            credentials {
                username = providers.gradleProperty("artifactory_user")
                    .getOrElse(System.getenv("JFROG_USER").orEmpty())
                password = providers.gradleProperty("artifactory_password")
                    .getOrElse(System.getenv("JFROG_PASSWORD").orEmpty())
            }
        }
    }
}

dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://gluoneurope.jfrog.io/artifactory/ods-maven-android-public/")
            credentials {
                username = providers.gradleProperty("artifactory_user")
                    .getOrElse(System.getenv("JFROG_USER").orEmpty())
                password = providers.gradleProperty("artifactory_password")
                    .getOrElse(System.getenv("JFROG_PASSWORD").orEmpty())
            }
        }
    }
    versionCatalogs { register("libs") { from(files("../gradle/libs.versions.toml")) } }
}

// Keep in sync with ../settings.gradle.kts
buildCache { local { directory = rootDir.parentFile.resolve(".gradle/build-cache") } }

include(":katana-convention")
