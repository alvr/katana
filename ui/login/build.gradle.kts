plugins {
    id("modules.compose-library")
}

dependencies {
    implementation(libs.lifecycle.viewmodel)
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    debugImplementation(libs.compose.ui.test.manifest)
    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}
