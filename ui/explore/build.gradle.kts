import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.compose.library")
}

android.namespace = "${KatanaConfiguration.PackageName}.ui.explore"

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "explore")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.explore)
    implementation(projects.ui.base)

    testImplementation(projects.common.tests)
}
