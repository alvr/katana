import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.common.tests"

dependencies {
    api(projects.common.tests)

    implementation(platform(libs.compose.bom))
    implementation(libs.bundles.core.common.test)
    implementation(libs.bundles.core.jvm.test)
    implementation(libs.bundles.mobile.android.test)
    implementation(libs.bundles.ui.android.test)
}
