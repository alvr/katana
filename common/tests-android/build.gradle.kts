import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.test.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.common.tests"

dependencies {
    api(projects.common.tests)

    implementation(libs.bundles.test.android)
}
