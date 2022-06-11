package modules

import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.library
    `kotlin-android`
    `kotlin-kapt`
    dagger.hilt.android.plugin
    id("plugins.sonarqube-android")
}

hilt.enableAggregatingTask = true
kapt.correctErrorTypes = true

android {
    baseAndroidConfig()
    buildFeatures.buildConfig = false
    kotlinOptions.configureKotlin()

    libraryVariants.all {
        kotlin.sourceSets {
            getByName(name) {
                kotlin.srcDir("build/generated/ksp/$name/kotlin")
            }
        }
    }
}
