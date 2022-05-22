import java.io.FileInputStream
import java.util.Properties
import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.application
    com.google.dagger.hilt.android
    `kotlin-android`
    `kotlin-kapt`
    plugins.`sonarqube-android`
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
    kotlinOptions.configureKotlin()

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.data.preferences.token)

    implementation(projects.data.remote.base)
    implementation(projects.data.remote.lists)
    implementation(projects.data.remote.user)

    implementation(projects.domain.base)
    implementation(projects.domain.lists)
    implementation(projects.domain.token)
    implementation(projects.domain.user)

    implementation(projects.ui.base)
    implementation(projects.ui.login)
    implementation(projects.ui.lists)
    implementation(projects.ui.social)
    implementation(projects.ui.explore)
    implementation(projects.ui.account)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.app)

    kapt(libs.bundles.kapt)

    debugImplementation(libs.leakcanary)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
    kaptAndroidTest(libs.bundles.kapt)
}

fun Properties.getValue(key: String, env: String) =
    getOrElse(key) { System.getenv(env) } as? String
