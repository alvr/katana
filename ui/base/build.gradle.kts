plugins {
    id("modules.compose-library")
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    implementation(libs.accompanist.systemuicontroller)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
}
