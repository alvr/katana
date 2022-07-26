plugins {
    modules.`compose-library`
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "login")
}

dependencies {
    implementation(projects.domain.session)
    implementation(projects.domain.user)
    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    kapt(libs.bundles.kapt.ui)
    ksp(libs.bundles.ksp.ui)

    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
}
