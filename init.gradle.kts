import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent { snapshotsOnly() }
        }
    }
    dependencies { classpath("org.gradle:github-dependency-graph-gradle-plugin:1.4.1") }
}

beforeSettings {
    caches {
        cleanup = Cleanup.ALWAYS
        buildCache.setRemoveUnusedEntriesAfterDays(30)
    }
}

apply<GitHubDependencyGraphPlugin>()
