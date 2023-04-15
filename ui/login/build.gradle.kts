import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.multiplatform.compose")
}

android.namespace = "${KatanaConfiguration.PackageName}.ui.login"

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "login")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)
    implementation(projects.domain.user)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
