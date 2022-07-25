plugins {
    `kotlinx-serialization`
    modules.`android-library`
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.domain.session)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    kapt(libs.bundles.kapt)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.android)
    kaptTest(libs.bundles.kapt)
}
