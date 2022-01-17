package modules

plugins {
    id("modules.android-library")
}

val composeCompiler: String by rootProject.extra

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = composeCompiler

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}
