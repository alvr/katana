package modules

import utils.baseAndroidConfig
import utils.configureKotlin

plugins {
    com.android.library
    `kotlin-android`
}

android {
    baseAndroidConfig()
    kotlinOptions.configureKotlin()
}
