package dev.alvr.katana.buildlogic.gradle

import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import dev.alvr.katana.buildlogic.ConventionPlugin
import java.util.Locale
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.withType

class DependencyVersionsConventionPlugin : ConventionPlugin {
    private val stableKeywords = arrayOf("RELEASE", "FINAL", "GA")
    private val releaseRegex = "^[0-9,.v-]+(-r)?$".toRegex(RegexOption.IGNORE_CASE)
    private val rcRegex = regex("rc")
    private val betaRegex = regex("beta")
    private val alphaRegex = regex("alpha")
    private val devRegex = regex("dev")

    private fun regex(keyword: String) =
        "^[0-9,.v-]+(-$keyword[0-9]*)$".toRegex(RegexOption.IGNORE_CASE)

    private enum class ReleaseType {
        SNAPSHOT,
        DEV,
        ALPHA,
        BETA,
        RC,
        RELEASE;

        fun isLessStableThan(other: ReleaseType): Boolean = ordinal < other.ordinal
    }

    private fun checkDependencyVersion(version: String) =
        if (stableKeywords.any { version.toUpperCase(Locale.ROOT).contains(it) }) {
            ReleaseType.RELEASE
        } else {
            when {
                releaseRegex.matches(version) -> ReleaseType.RELEASE
                rcRegex.matches(version) -> ReleaseType.RC
                betaRegex.matches(version) -> ReleaseType.BETA
                alphaRegex.matches(version) -> ReleaseType.ALPHA
                devRegex.matches(version) -> ReleaseType.DEV
                else -> ReleaseType.SNAPSHOT
            }
        }

    override fun Project.configure() {
        apply(plugin = "com.github.ben-manes.versions")

        tasks.withType<DependencyUpdatesTask> {
            rejectVersionIf {
                @Suppress("UseIfInsteadOfWhen")
                when (val current = checkDependencyVersion(currentVersion)) {
                    ReleaseType.SNAPSHOT -> true // We are using a SNAPSHOT for a reason
                    else -> checkDependencyVersion(candidate.version).isLessStableThan(current)
                }
            }
        }
    }
}
