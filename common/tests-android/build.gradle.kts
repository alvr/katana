import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.test.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.common.tests"

dependencies {
    api(projects.common.tests)

    implementation(libs.bundles.test)
    implementation(libs.bundles.test.android)
    implementation(libs.bundles.test.ui)
}
