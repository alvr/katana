package plugins

import com.github.benmanes.gradle.versions.VersionsPlugin
import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import utils.ReleaseType
import utils.checkDependencyVersion

apply<VersionsPlugin>()

tasks.withType<DependencyUpdatesTask> {
    rejectVersionIf {
        @Suppress("UseIfInsteadOfWhen")
        when (val current = checkDependencyVersion(currentVersion)) {
            ReleaseType.SNAPSHOT -> true // We are using a SNAPSHOT for a reason
            else -> checkDependencyVersion(candidate.version).isLessStableThan(current)
        }
    }
}
