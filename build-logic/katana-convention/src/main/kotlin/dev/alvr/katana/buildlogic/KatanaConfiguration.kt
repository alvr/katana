package dev.alvr.katana.buildlogic

import org.gradle.api.JavaVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget as KtJvmTarget
import org.jetbrains.kotlin.gradle.dsl.KotlinVersion as KtVersion

internal object KatanaConfiguration {
    const val CompileSdk = 36
    const val CompileSdkMinor = 1
    const val BuildTools = "36.1.0"
    const val PackageName = "dev.alvr.katana"
    const val MinSdk = 23
    const val TargetSdk = 36
    const val VersionName = "0.0.1"
    const val VersionCode = 1

    val UseJavaVersion = JavaVersion.VERSION_21
    val JvmTarget = KtJvmTarget.fromTarget(UseJavaVersion.toString())
    val JvmTargetStr = JvmTarget.target
    val KotlinVersion = KtVersion.KOTLIN_2_2
}
