package dev.alvr.katana.buildlogic.multiplatform

import com.android.build.gradle.BaseExtension
import com.android.build.gradle.LibraryExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.KatanaConfiguration
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.get
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal class KatanaMultiplatformMobilePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.kotlin.native.cocoapods")
        apply(plugin = "com.android.library")
        apply(plugin = "com.google.devtools.ksp")

        with(extensions) {
            create<KatanaMultiplatformMobileExtension>(KATANA_MULTIPLATFORM_EXTENSION)

            configure<KotlinMultiplatformExtension> { configure(project) }
            getByType<LibraryExtension>().configureAndroid()
        }
    }

    private fun KotlinMultiplatformExtension.configure(project: Project) {
        android()
        ios()
        iosSimulatorArm64()

        configureSourceSets()
        configureCocoapods(project)
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting
            val androidMain by getting { dependsOn(commonMain) }
            val iosMain by getting { dependsOn(commonMain) }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting
            val androidUnitTest by getting { dependsOn(commonTest) }
            val iosTest by getting { dependsOn(commonTest) }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }

    private fun BaseExtension.configureAndroid() {
        with(sourceSets["main"]) {
            res.srcDirs("src/androidMain/res")
            resources.srcDir("src/commonMain/resources")
        }
        defaultConfig {
            minSdk = KatanaConfiguration.MinSdk
            targetSdk = KatanaConfiguration.TargetSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    // TODO context(KotlinMultiplatformExtension)
    private fun KotlinMultiplatformExtension.configureCocoapods(project: Project) {
        configureExtension<CocoapodsExtension> {
            version = KatanaConfiguration.VersionName
            homepage = "https://github.com/alvr/katana"
            ios.deploymentTarget = "14.1"
            podfile = project.rootDir.resolve("./iosApp/Podfile")
            framework {
                baseName = project.path.removePrefix(":").replace(':', '_')
                isStatic = true
            }
            extraSpecAttributes["resources"] = "[$COMMON_RESOURCES_DIR, $IOS_RESOURCES_DIR]"
        }
    }
}
