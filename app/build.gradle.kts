import java.io.FileInputStream
import java.util.Properties
import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.application
    `kotlin-android`
    `kotlin-kapt`
    id("com.google.dagger.hilt.android")
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
        create("release") {
            val props = Properties().also { p ->
                runCatching {
                    FileInputStream(rootProject.file("local.properties")).use { f ->
                        p.load(f)
                    }
                }
            }

            enableV3Signing = true
            enableV4Signing = true

            keyAlias = props.getValue("signingAlias", "SIGNING_ALIAS")
            keyPassword = props.getValue("signingAliasPass", "SIGNING_ALIAS_PASS")
            storeFile = props.getValue("signingFile", "SIGNING_FILE")?.let { rootProject.file(it) }
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
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.get()

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }

    kotlinOptions.configureKotlin()
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.data.preferences.token)

    implementation(projects.data.remote.base)
    implementation(projects.data.remote.user)

    implementation(projects.domain.base)
    implementation(projects.domain.token)
    implementation(projects.domain.user)

    implementation(projects.ui.base)
    implementation(projects.ui.login)
    implementation(projects.ui.lists)
    implementation(projects.ui.social)
    implementation(projects.ui.explore)
    implementation(projects.ui.account)

    implementation(libs.accompanist.insets)
    implementation(libs.accompanist.insets.ui)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.app)

    kapt(libs.bundles.kapt)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}

fun Properties.getValue(key: String, env: String) =
    getOrElse(key) { System.getenv(env) } as? String
