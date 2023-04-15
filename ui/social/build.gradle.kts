import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.multiplatform.compose")
}

android.namespace = "${KatanaConfiguration.PackageName}.ui.social"

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "social")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.social)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
