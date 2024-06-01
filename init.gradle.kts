import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.gradle:github-dependency-graph-gradle-plugin:1.3.0")
    }
}

beforeSettings {
    caches {
        cleanup = Cleanup.ALWAYS
        buildCache.setRemoveUnusedEntriesAfterDays(30)
    }
}

apply<GitHubDependencyGraphPlugin>()
