plugins {
    modules.`android-library`
}

dependencies {
    api(projects.common.tests)

    implementation(libs.bundles.common.android)

    kapt(libs.bundles.kapt)

    implementation(libs.bundles.test.android)
}
