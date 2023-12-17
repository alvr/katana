package dev.alvr.katana.buildlogic.mp.core

import dev.alvr.katana.buildlogic.mp.configureSourceSets
import javax.inject.Inject
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinDependencyHandler

open class KatanaMultiplatformCoreExtension @Inject constructor(project: Project) {
    private val multiplatformProject by lazy {
        project.extensions.getByType<KotlinMultiplatformExtension>()
    }

    fun commonMainDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("commonMain", dependencies)
    }

    fun commonTestDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("commonTest", dependencies)
    }

    fun jvmMainDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("jvmMain", dependencies)
    }

    fun jvmTestDependencies(dependencies: KotlinDependencyHandler.() -> Unit) {
        configureSourceSet("jvmTest", dependencies)
    }

    protected fun configureSourceSet(name: String, dependencies: KotlinDependencyHandler.() -> Unit) {
        multiplatformProject.configureSourceSets {
            getByName(name) {
                dependencies(dependencies)
            }
        }
    }
}
