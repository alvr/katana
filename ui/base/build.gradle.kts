plugins {
    id("modules.compose-library")
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.compose.fonts)

    kapt(libs.bundles.kapt.ui)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
    kaptAndroidTest(libs.bundles.kapt)
}
