import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.gradle:github-dependency-graph-gradle-plugin:1.3.1")
    }
}

beforeSettings {
    caches {
        cleanup = Cleanup.ALWAYS
        buildCache.setRemoveUnusedEntriesAfterDays(30)
    }
}

apply<GitHubDependencyGraphPlugin>()
