import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.data.preferences.base"

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.base)

    implementation(libs.bundles.data.preferences)

    testImplementation(projects.common.testsAndroid)
    kaptTest(libs.bundles.kapt)
}
