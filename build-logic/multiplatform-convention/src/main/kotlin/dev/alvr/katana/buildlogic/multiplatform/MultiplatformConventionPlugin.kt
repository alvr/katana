package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.plugins.ExtensionAware
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal class MultiplatformConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
    }
}
