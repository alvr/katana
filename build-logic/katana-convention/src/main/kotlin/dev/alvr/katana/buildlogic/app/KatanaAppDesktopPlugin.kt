@file:Suppress("NoUnusedImports", "UnusedImports")

package dev.alvr.katana.buildlogic.app

import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.mp.configureDesktop
import dev.alvr.katana.buildlogic.mp.configureKotlin
import dev.alvr.katana.buildlogic.mp.desktopMain
import dev.alvr.katana.buildlogic.mp.desktopTest
import java.time.Year
import kotlin.text.Typography.copyright
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.declarative.dsl.schema.FqName.Empty.packageName
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.desktop.DesktopExtension
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaAppDesktopPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "org.jetbrains.kotlin.plugin.compose")

        extensions.configure<KotlinMultiplatformExtension> { configureMultiplatform() }
        extensions.configure<ComposeExtension> { configureCompose(project) }

        tasks.commonTasks()
    }

    private fun KotlinMultiplatformExtension.configureMultiplatform() {
        configureDesktop()
        configureSourceSets()

        applyDefaultHierarchyTemplate()

        configureKotlin()
    }

    private fun KotlinMultiplatformExtension.configureSourceSets() {
        val compose = (this as ExtensionAware).extensions.getByType<ComposePlugin.Dependencies>()

        sourceSets {
            commonMain.dependencies {
                implementation(compose.runtime)
            }

            desktopMain.dependencies {
                implementation(compose.desktop.currentOs)
                implementation(catalogBundle("app-jvm"))
                implementation(catalogBundle("mobile-common"))
                implementation(catalogBundle("mobile-jvm"))
                implementation(project(":shared"))
            }

            desktopTest.dependencies {
                implementation(catalogBundle("mobile-common-test"))
                implementation(catalogBundle("mobile-jvm-test"))
            }
        }
    }

    private fun ComposeExtension.configureCompose(project: Project) {
        val desktop = (this as ExtensionAware).extensions.getByType<DesktopExtension>()

        desktop.application {
            mainClass = "dev.alvr.katana.KatanaKt"
            nativeDistributions {
                targetFormats(
                    TargetFormat.Deb,
                    TargetFormat.Rpm,
                    TargetFormat.Dmg,
                    TargetFormat.Exe,
                )
                packageName = "Katana"
                packageVersion = KatanaConfiguration.VersionName
                copyright = "© 2022 - ${Year.now()} Álvaro Salcedo García (alvr). Licensed under the Apache License."
                licenseFile.set(project.rootProject.file("LICENSE"))
            }
        }
    }
}
