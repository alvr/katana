plugins {
    modules.`compose-library`
}

dependencies {
    implementation(projects.common.core)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.compose.fonts)

    kapt(libs.bundles.kapt.ui)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
}
