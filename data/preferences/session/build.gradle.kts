import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
    alias(libs.plugins.serialization)
}

android.namespace = "${KatanaConfiguration.PackageName}.data.preferences.session"

dependencies {
    implementation(projects.common.core)
    implementation(projects.data.preferences.base)
    implementation(projects.domain.session)

    implementation(libs.bundles.data.preferences)

    testImplementation(projects.common.testsAndroid)
    kaptTest(libs.bundles.kapt)
}
