import org.gradle.github.GitHubDependencyGraphPlugin

initscript {
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
    dependencies { classpath("org.gradle:github-dependency-graph-gradle-plugin:1.5.0") }
}

beforeSettings {
    caches {
        cleanup = Cleanup.ALWAYS
        buildCache.setRemoveUnusedEntriesAfterDays(30)
    }
}

apply<GitHubDependencyGraphPlugin>()
