package dev.alvr.katana.buildlogic.multiplatform

import javax.inject.Inject
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

open class KatanaMultiplatformMobileExtension @Inject constructor(
    project: Project
) : KatanaMultiplatformCoreExtension(project) {
    fun androidMainDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("androidMain", dependencies)
    }

    fun androidTestDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("androidTest", dependencies)
    }

    fun iosMainDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("iosMain", dependencies)
    }

    fun iosTestDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("iosTest", dependencies)
    }
}
