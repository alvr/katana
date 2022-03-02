plugins {
    id("modules.compose-library")
}

dependencies {
    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    kapt(libs.bundles.kapt.ui)

    debugImplementation(libs.compose.ui.test.manifest)
    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}
