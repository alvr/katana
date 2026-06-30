import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
    repositories {
        gradlePluginPortal()
        maven {
            url = uri("https://central.sonatype.com/repository/maven-snapshots/")
            mavenContent { snapshotsOnly() }
        }
    }
    dependencies { classpath(libs.gradle.github.dependency.graph) }
}

beforeSettings {
    caches {
        cleanup = Cleanup.ALWAYS
        buildCache.setRemoveUnusedEntriesAfterDays(30)
    }
}

apply<GitHubDependencyGraphPlugin>()
