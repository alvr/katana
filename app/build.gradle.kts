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
    implementation(libs.androidx.activity)
    implementation(libs.androidx.core)

    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.design.material)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.compose.ui.test)
}
