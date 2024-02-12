import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
    repositories {
        maven {
            url = uri("https://plugins.gradle.org/m2/")
        }
    }
    dependencies {
        classpath("org.gradle:github-dependency-graph-gradle-plugin:1.2.1")
    }
}

apply<GitHubDependencyGraphPlugin>()
