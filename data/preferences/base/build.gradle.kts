plugins {
    modules.`android-library`
}

dependencies {
    implementation(projects.domain.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    kapt(libs.bundles.kapt)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    kaptAndroidTest(libs.bundles.kapt)
}
