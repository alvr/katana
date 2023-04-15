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
import org.jetbrains.compose.ComposePlugin
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.plugin.KotlinSourceSet
import org.jetbrains.kotlin.gradle.plugin.cocoapods.CocoapodsExtension

internal class MultiplatformComposeConventionPlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "org.jetbrains.kotlin.multiplatform")
        apply(plugin = "org.jetbrains.compose")
        apply(plugin = "org.jetbrains.kotlin.native.cocoapods")
        apply(plugin = "com.android.library")
        apply(plugin = "com.google.devtools.ksp")
        apply(plugin = "katana.sonarqube.kotlin")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configure(this@configure) }
            getByType<LibraryExtension>().base()
        }
    }

    private fun KotlinMultiplatformExtension.configure(project: Project) {
        android()
        ios()
        iosSimulatorArm64()

        configureCocoapods(project)
        configureSourceSets()
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

    @OptIn(ExperimentalComposeLibrary::class)
    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureExtension<NamedDomainObjectContainer<KotlinSourceSet>> {
            val commonMain by getting {
                dependencies {
                    implementation(compose.runtime)
                    implementation(compose.material)
                    implementation(compose.foundation)
                }
            }
            val commonTest by getting

            val androidMain by getting
            val androidUnitTest by getting

            val iosMain by getting
            val iosTest by getting

            val iosSimulatorArm64Main by getting {
                dependsOn(iosMain)
            }
            val iosSimulatorArm64Test by getting {
                dependsOn(iosTest)
            }
        }
    }

    private fun LibraryExtension.base() {
        compileSdk = KatanaConfiguration.CompileSdk
        sourceSets["main"].res.srcDirs("src/androidMain/res")
        sourceSets["main"].resources.srcDir("src/commonMain/resources")
        defaultConfig {
            minSdk = KatanaConfiguration.MinSdk
            targetSdk = KatanaConfiguration.TargetSdk
        }
        compileOptions {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }

    private inline fun <reified T : Any> KotlinMultiplatformExtension.configureExtension(
        noinline block: T.() -> Unit
    ) {
        (this as ExtensionAware).extensions.configure(block)
    }

    private val KotlinMultiplatformExtension.compose get() =
        (this as ExtensionAware).extensions.getByName("compose") as ComposePlugin.Dependencies
}
