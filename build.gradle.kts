import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask

plugins {
    alias(libs.plugins.updates)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        when (val current = checkDependencyVersion(currentVersion)) {
            ReleaseType.SNAPSHOT -> true // We are using a SNAPSHOT for a reason
            else -> checkDependencyVersion(candidate.version).isLessStableThan(current)
        }
    }
}
