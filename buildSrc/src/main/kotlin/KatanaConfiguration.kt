@file:Suppress("InvalidPackageDeclaration")

import org.gradle.api.JavaVersion

object KatanaConfiguration {
    const val CompileSdk = 31
    const val BuildTools = "31.0.0"
    const val PackageName = "dev.alvr.starter"
    const val MinSdk = 21
    const val TargetSdk = 31
    const val VersionName = "0.0.1"
    const val VersionCode = 1

    val UseJavaVersion = JavaVersion.VERSION_11
    const val JvmTarget = "11"
    const val KotlinVersion = "1.6"
}
