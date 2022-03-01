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

include(":app")

// Include all modules in these directories
listOf("data/preferences", "data/remote", "domain", "ui").forEach { topDir ->
    rootDir.resolve(topDir)
        .walkTopDown()
        .maxDepth(1)
        .filter { file ->
            file.isDirectory && file.resolve("build.gradle.kts").exists()
        }.forEach { module ->
            include(":${topDir.replace('/', ':')}:${module.name}")
        }
}

enableFeaturePreview("VERSION_CATALOGS")
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
