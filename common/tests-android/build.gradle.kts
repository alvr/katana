import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.common.tests"

dependencies {
    api(projects.common.tests)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.common.test)
    implementation(libs.bundles.common.test.jvm)
    implementation(libs.bundles.common.test.android)
    implementation(libs.bundles.test.ui)
}
