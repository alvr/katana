import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.configureKotlin

plugins {
    plugins.dependencies
    plugins.detekt
    plugins.kover
    plugins.sonarqube
}

// Version catalogs is not accessible from precompile scripts
// https://github.com/gradle/gradle/issues/15383
buildscript {
    extra.set("composeCompiler", libs.versions.compose.compiler.get())
    extra.set("detektFormatting", libs.detekt.formatting)
    extra.set("intellijEngine", libs.versions.intellij.get())
}

allprojects {
    tasks.withType<KotlinCompile> {
        kotlinOptions.configureKotlin()
    }
}

tasks {
    register<Delete>("clean") {
        delete(buildDir)
    }

    register("unitTests") {
        val androidUnitTest = "testDebugUnitTest"
        val kotlinUnitTest = "test"

        subprojects.forEach { p ->
            if (p.tasks.findByName(androidUnitTest) != null) {
                dependsOn("${p.path}:$androidUnitTest")
            } else if (p.tasks.findByName(kotlinUnitTest) != null) {
                dependsOn("${p.path}:$kotlinUnitTest")
            }
        }
    }

    register<TestReport>("testMergedReport") {
        destinationDir = file("$buildDir/reports/allTests")
        reportOn(
            subprojects.map { p ->
                p.tasks.withType<Test>().matching { t ->
                    !t.name.contains("release", ignoreCase = true)
                }
            }
        )
    }
}
