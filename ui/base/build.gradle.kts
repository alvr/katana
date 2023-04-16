import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.compose.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.ui.base"

dependencies {
    implementation(projects.common.core)

    implementation(libs.accompanist.systemuicontroller)

    testImplementation(projects.common.tests)
}
