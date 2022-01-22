import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.application
    `kotlin-android`
}

android {
    baseAndroidConfig()

    defaultConfig {
        applicationId = KatanaConfiguration.PackageName
        versionCode = KatanaConfiguration.VersionCode
        versionName = KatanaConfiguration.VersionName
    }

    buildTypes {
        debug {
            isDebuggable = true
            isDefault = true
            isMinifyEnabled = false
            isTestCoverageEnabled = true
        }

        release {
            isDebuggable = false
            isDefault = false
            isMinifyEnabled = true
            isTestCoverageEnabled = false
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
    implementation(projects.data.database)
    implementation(projects.data.preferences)
    implementation(projects.data.remote)

    implementation(projects.domain)

    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.app)
    implementation(libs.compose.ui)

    debugImplementation(libs.leakcanary)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}
