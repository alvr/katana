package dev.alvr.katana.buildlogic.kotlin

import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.commonTasks
import dev.alvr.katana.buildlogic.implementation
import dev.alvr.katana.buildlogic.kapt
import dev.alvr.katana.buildlogic.testImplementation
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByName

internal class KotlinLibraryConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.jvm")
        apply(plugin = "org.jetbrains.kotlin.kapt")
        apply(plugin = "katana.sonarqube.kotlin")

        extensions.commonExtensions()

        with(tasks) {
            commonTasks()

            getByName<Test>("test") {
                useJUnitPlatform()
            }
        }

        dependencies {
            implementation(catalogBundle("common"))

            kapt(catalogBundle("kapt"))

            testImplementation(catalogBundle("test"))
        }
    }
}
