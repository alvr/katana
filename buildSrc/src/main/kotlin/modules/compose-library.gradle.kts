package modules

plugins {
    com.google.devtools.ksp
    id("modules.android-library")
}

val composeCompiler: String by rootProject.extra

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = composeCompiler

    kotlinOptions {
        freeCompilerArgs = freeCompilerArgs + buildComposeParameters()
    }
}

fun Project.buildComposeParameters(): List<String> {
    val parameters = mutableListOf<String>()

    val enableMetricsProvider = project.providers.gradleProperty("enableComposeCompilerMetrics")
    if (enableMetricsProvider.orNull == "true") {
        val metricsFolder = File(project.buildDir, "compose-metrics")
        parameters.add("-P")
        parameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=" + metricsFolder.absolutePath,
        )
    }

    val enableReportsProvider = project.providers.gradleProperty("enableComposeCompilerReports")
    if (enableReportsProvider.orNull == "true") {
        val reportsFolder = File(project.buildDir, "compose-reports")
        parameters.add("-P")
        parameters.add(
            "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=" + reportsFolder.absolutePath,
        )
    }
    return parameters.toList()
}
