import com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask
import io.gitlab.arturbosch.detekt.Detekt

plugins {
    alias(libs.plugins.detekt)
    alias(libs.plugins.updates)
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")
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

tasks.register<Detekt>("detektAll") {
    description = "Run detekt in all modules"

    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/config/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")

    reports {
        html.required.set(true)
        sarif.required.set(false)
        txt.required.set(false)
        xml.required.set(false)
    }
}

dependencies {
    detektPlugins(libs.detekt.formatting)
}
