plugins {
    `kotlinx-serialization`
    modules.`android-library`
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.domain.token)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    kapt(libs.bundles.kapt)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    kaptAndroidTest(libs.bundles.kapt)
}
