import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.compose.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.ui.account"

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "account")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
