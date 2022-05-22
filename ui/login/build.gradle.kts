plugins {
    modules.`compose-library`
}

dependencies {
    implementation(projects.domain.token)
    implementation(projects.domain.user)
    implementation(projects.ui.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    kapt(libs.bundles.kapt.ui)

    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    androidTestImplementation(libs.bundles.test.ui)
    kaptAndroidTest(libs.bundles.kapt)
}
