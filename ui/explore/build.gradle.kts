plugins {
    modules.`compose-library`
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "explore")
}

dependencies {
    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    kapt(libs.bundles.kapt.ui)
    ksp(libs.bundles.ksp.ui)

    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)
}
