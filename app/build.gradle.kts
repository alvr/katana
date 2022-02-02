import java.io.FileInputStream
import java.util.Properties
import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.application
    `kotlin-android`
    `kotlin-kapt`
    id("dagger.hilt.android.plugin")
}

kapt.correctErrorTypes = true

android {
    baseAndroidConfig()

    defaultConfig {
        applicationId = KatanaConfiguration.PackageName
        versionCode = KatanaConfiguration.VersionCode
        versionName = KatanaConfiguration.VersionName
    }

    signingConfigs {
        register("release") {
            val props = Properties().also { p ->
                FileInputStream(rootProject.file("local.properties")).use { f -> p.load(f) }
            }

            enableV3Signing = true
            enableV4Signing = true

            keyAlias = props.getValue("signingAlias", "SIGNING_ALIAS")
            keyPassword = props.getValue("signingAliasPass", "SIGNING_ALIAS_PASS")
            storeFile = rootProject.file(props.getValue("signingFile", "SIGNING_FILE"))
            storePassword = props.getValue("signingFilePass", "SIGNING_FILE_PASS")
        }
    }

    buildTypes {
        debug {
            isDebuggable = true
            isDefault = true
            isMinifyEnabled = false
            isShrinkResources = false
            isTestCoverageEnabled = true
        }

        release {
            isDebuggable = false
            isDefault = false
            isMinifyEnabled = true
            isShrinkResources = true
            isTestCoverageEnabled = false

            signingConfig = signingConfigs.getByName("release")
        }
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    kotlinOptions.configureKotlin()
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.data.remote)
    implementation(projects.domain)
    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.app)
    implementation(libs.compose.ui)

    kapt(libs.bundles.kapt)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}

fun Properties.getValue(key: String, env: String) =
    getOrElse(key) { System.getenv(env) } as String
