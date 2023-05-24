package dev.alvr.katana.buildlogic.android

import com.android.build.gradle.BaseExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import dev.alvr.katana.buildlogic.commonExtensions
import dev.alvr.katana.buildlogic.isRelease
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.kotlin.dsl.systemProperties

internal fun ExtensionContainer.commonAndroidExtensions() {
    commonExtensions()
}

internal fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        vectorDrawables.useSupportLibrary = true
    }

    buildFeatures.buildConfig = false

    buildTypes {
        getByName("release") {
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
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
                test.enabled = !test.isRelease
                test.jvmArgs = listOf("-Xmx8G")
                test.systemProperties(
                    "robolectric.usePreinstrumentedJars" to "true",
                    "robolectric.logging.enabled" to "true",
                )
            }
        }
    }
}
