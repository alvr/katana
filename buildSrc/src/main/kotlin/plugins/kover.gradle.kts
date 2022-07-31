package plugins

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.CoverageEngine
import kotlinx.kover.api.KoverExtension
import kotlinx.kover.tasks.KoverMergedHtmlReportTask
import kotlinx.kover.tasks.KoverMergedVerificationTask
import kotlinx.kover.tasks.KoverXmlReportTask

apply<KoverPlugin>()

val intellijEngine: String by rootProject.extra

val koverIncludes = listOf("dev.alvr.katana.*")
val koverExcludes = listOf(
    // Anonymous
    "*$$*",

    // App
    "*.KatanaApp",
    "*.initializers.*",

    // Apollo
    "*.remote.*.adapter.*",
    "*.remote.*.fragment.*",
    "*.remote.*.selections.*",
    "*.remote.*.type.*",
    "*.remote.*.*Mutation*",
    "*.remote.*.*Query*",

    // Common
    "*.common.*",

    // Common Android
    "*.BuildConfig",
    "*.*Activity",
    "*.*Fragment",
    "*.base.*",
    "*.navigation.*",

    // Compose
    "*.*ComposableSingletons*",

    // Hilt
    "*.di.*",
    "*.*Hilt_*",
    "*.*HiltModules*",
    "*.*_Factory",

    // Ui
    "*.ui.*.components.*",
    "*.ui.*.view.*",
)

val koverMinCoveredLines = 80

tasks {
    withType<KoverXmlReportTask> {
        includes = koverIncludes
        excludes = koverExcludes
    }

    getByName<KoverMergedHtmlReportTask>("koverMergedHtmlReport") {
        includes = koverIncludes
        excludes = koverExcludes
    }

    getByName<KoverMergedVerificationTask>("koverMergedVerify") {
        includes = koverIncludes
        excludes = koverExcludes

        rule {
            name = "Minimal line coverage rate in percent"
            bound {
                minValue = koverMinCoveredLines
            }
        }
    }
}

configure<KoverExtension> {
    coverageEngine.set(CoverageEngine.INTELLIJ)
    intellijEngineVersion.set(intellijEngine)
    instrumentAndroidPackage = true
    runAllTestsForProjectTask = true
}
