import kotlinx.kover.api.CoverageEngine
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.KOVER_MIN_COVERED_LINES
import utils.configureKotlin
import utils.koverExcludes
import utils.koverIncludes

plugins {
    plugins.dependencies
    plugins.detekt
    alias(libs.plugins.kover)
}

// Version catalogs is not accessible from precompile scripts
// https://github.com/gradle/gradle/issues/15383
buildscript {
    extra.set("composeCompiler", libs.versions.compose.get())
    extra.set("detektFormatting", libs.detekt.formatting)
}

tasks.koverMergedHtmlReport {
    includes = koverIncludes
    excludes = koverExcludes
}

tasks.koverMergedVerify {
    includes = koverIncludes
    excludes = koverExcludes

    rule {
        name = "Minimal line coverage rate in percent"
        bound {
            minValue = KOVER_MIN_COVERED_LINES
        }
    }
}

tasks.register<Delete>("clean") {
    delete(buildDir)
}

tasks.withType<KotlinCompile> {
    kotlinOptions.configureKotlin()
}

kover {
    coverageEngine.set(CoverageEngine.INTELLIJ)
    jacocoEngineVersion.set(libs.versions.intellij.get())
    instrumentAndroidPackage = true
}
