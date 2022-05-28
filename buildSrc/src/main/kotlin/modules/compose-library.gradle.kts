package modules

plugins {
    com.google.devtools.ksp
    id("modules.android-library")
}

val composeCompiler: String by rootProject.extra

android {
    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = composeCompiler
}
