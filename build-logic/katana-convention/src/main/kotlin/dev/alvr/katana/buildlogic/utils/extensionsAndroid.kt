package dev.alvr.katana.buildlogic.utils

import com.android.build.gradle.BaseExtension
import dev.alvr.katana.buildlogic.KatanaConfiguration
import org.gradle.kotlin.dsl.get

internal fun BaseExtension.configureAndroid(packageName: String) {
    compileSdkVersion(KatanaConfiguration.CompileSdk)
    buildToolsVersion(KatanaConfiguration.BuildTools)

    buildFeatures.buildConfig = false
    namespace = packageName

    defaultConfig {
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk
        versionCode = KatanaConfiguration.VersionCode
        versionName = KatanaConfiguration.VersionName

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = KatanaConfiguration.UseJavaVersion
        targetCompatibility = KatanaConfiguration.UseJavaVersion
    }

    with(sourceSets["main"]) {
        res.srcDirs("$AndroidDir/res", ResourcesDir)
        resources.srcDirs(ResourcesDir)
    }

    testOptions {
        animationsDisabled = true
        unitTests {
            isIncludeAndroidResources = true
            all { test ->
                test.useJUnitPlatform()
                test.enabled = !test.isRelease
            }
        }
    }
}
