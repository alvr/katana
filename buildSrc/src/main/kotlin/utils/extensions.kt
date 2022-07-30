package utils

import KatanaConfiguration
import com.android.build.gradle.BaseExtension
import kotlinx.kover.api.KoverTaskExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.systemProperties
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            consumerProguardFile("consumer-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = KatanaConfiguration.UseJavaVersion
        targetCompatibility = KatanaConfiguration.UseJavaVersion
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.useJUnitPlatform()
                test.extensions.configure<KoverTaskExtension> {
                    isDisabled = test.name != "testDebugUnitTest"
                }
                test.jvmArgs = listOf("-noverify", "-Xmx8G")
                test.systemProperties(
                    "robolectric.usePreinstrumentedJars" to "true",
                    "robolectric.logging.enabled" to "true",
                )
            }
        }
    }
}

fun KotlinJvmOptions.configureKotlin() {
    jvmTarget = KatanaConfiguration.JvmTarget
    apiVersion = KatanaConfiguration.KotlinVersion
    languageVersion = KatanaConfiguration.KotlinVersion
    freeCompilerArgs = freeCompilerArgs + listOf("-opt-in=kotlin.RequiresOptIn")
}
