package dev.alvr.katana.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget as KtJvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion as KtVersion

object KatanaConfiguration {
    const val CompileSdk = 33
    const val BuildTools = "33.0.1"
    const val PackageName = "dev.alvr.katana"
    const val MinSdk = 21
    const val TargetSdk = 33
    const val VersionName = "0.0.1"
    const val VersionCode = 1

    val UseJavaVersion = JavaVersion.VERSION_11
    val JvmTarget = KtJvmTarget.fromTarget(UseJavaVersion.toString())
    val JvmTargetStr = JvmTarget.target
    val JvmTargetInt = JvmTargetStr.toInt()
    val KotlinVersion = KtVersion.KOTLIN_1_8
}
