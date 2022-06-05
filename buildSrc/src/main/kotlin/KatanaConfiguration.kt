@file:Suppress("InvalidPackageDeclaration")

import org.gradle.api.JavaVersion

object KatanaConfiguration {
    const val CompileSdk = 32
    const val BuildTools = "32.0.0"
    const val PackageName = "dev.alvr.katana"
    const val MinSdk = 21
    const val TargetSdk = 32
    const val VersionName = "0.0.1"
    const val VersionCode = 1

    val UseJavaVersion = JavaVersion.VERSION_11
    const val JvmTarget = "11"
    const val KotlinVersion = "1.6"
}
