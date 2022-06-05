package utils

import KatanaConfiguration
import com.android.build.gradle.BaseExtension
import kotlinx.kover.api.KoverTaskExtension
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

fun BaseExtension.baseAndroidConfig() {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk

        testInstrumentationRunner = "dev.alvr.katana.utils.tests.android.HiltTestRunner"
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

    lintOptions {
        isAbortOnError = false
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
