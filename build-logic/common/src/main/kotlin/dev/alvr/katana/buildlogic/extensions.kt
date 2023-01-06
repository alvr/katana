package dev.alvr.katana.buildlogic

import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.api.tasks.testing.Test
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JvmVendorSpec
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmCompilerOptions
import org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension
import org.jetbrains.kotlin.gradle.plugin.KaptExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

private val Project.libs get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

val Test.isRelease get() = name.contains("""beta|release""".toRegex(RegexOption.IGNORE_CASE))

fun Project.catalogVersion(alias: String) = libs.findVersion(alias).get().toString()
fun Project.catalogLib(alias: String) = libs.findLibrary(alias).get()
fun Project.catalogBundle(alias: String) = libs.findBundle(alias).get()

fun DependencyHandlerScope.implementation(provider: Provider<*>) {
    addProvider("implementation", provider)
}

fun DependencyHandlerScope.debugImplementation(provider: Provider<*>) {
    addProvider("debugImplementation", provider)
}

fun DependencyHandlerScope.testImplementation(provider: Provider<*>) {
    addProvider("testImplementation", provider)
}

fun DependencyHandlerScope.kapt(provider: Provider<*>) {
    addProvider("kapt", provider)
}

fun DependencyHandlerScope.ksp(provider: Provider<*>) {
    addProvider("ksp", provider)
}

fun DependencyHandlerScope.detekt(provider: Provider<*>) {
    addProvider("detektPlugins", provider)
}

fun DependencyHandlerScope.desugaring(provider: Provider<*>) {
    addProvider("coreLibraryDesugaring", provider)
}

fun ExtensionContainer.commonExtensions() {
    configure<JavaPluginExtension> {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(KatanaConfiguration.JvmTargetStr))
            vendor.set(JvmVendorSpec.AZUL)
        }
    }

    configure<KotlinProjectExtension> {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(KatanaConfiguration.JvmTargetStr))
            vendor.set(JvmVendorSpec.AZUL)
        }
    }

    configure<KaptExtension> {
        correctErrorTypes = true
    }
}

fun TaskContainer.commonTasks() {
    withType<JavaCompile>().configureEach {
        options.release.set(KatanaConfiguration.JvmTargetInt)
    }
    withType<KotlinCompile>().configureEach {
        compilerOptions.configureKotlin()
    }
}

private fun KotlinJvmCompilerOptions.configureKotlin() {
    jvmTarget.set(KatanaConfiguration.JvmTarget)
    apiVersion.set(KatanaConfiguration.KotlinVersion)
    languageVersion.set(KatanaConfiguration.KotlinVersion)
    freeCompilerArgs.set(freeCompilerArgs.get() + listOf("-opt-in=kotlin.RequiresOptIn"))
}
