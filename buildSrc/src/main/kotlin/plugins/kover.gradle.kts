package plugins

import kotlinx.kover.KoverPlugin
import kotlinx.kover.api.CoverageEngine
import kotlinx.kover.api.KoverExtension
import kotlinx.kover.tasks.KoverMergedHtmlReportTask
import kotlinx.kover.tasks.KoverMergedVerificationTask
import kotlinx.kover.tasks.KoverXmlReportTask

apply<KoverPlugin>()

val intellijEngine: String by rootProject.extra

val koverIncludes = listOf(
    "dev.alvr.katana.navigation.screens.*",
    "dev.alvr.katana.*.*ViewModel",
    "dev.alvr.katana.data.*",
    "dev.alvr.katana.domain.*",
    "dev.alvr.katana.ui.*.*Effect",
    "dev.alvr.katana.ui.*.*State",
    "dev.alvr.katana.ui.*.*ViewModel",
)
val koverExcludes = listOf(
    // Anonymous
    "*$$*",

    // Apollo
    "*.remote.*.adapter.*",
    "*.remote.*.fragment.*",
    "*.remote.*.selections.*",
    "*.remote.*.type.*",
    "*.remote.*.*Query",

    // Common Android
    "*.BuildConfig",
    "*.base.*",

    // Compose
    "*.*ComposableSingletons*",

    // Hilt
    "*.di.*",
    "*.*Hilt_*",
    "*.*HiltModules*",
    "*.*_Factory",
)

val koverMinCoveredLines = 50

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
