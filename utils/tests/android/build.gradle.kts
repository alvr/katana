plugins {
    modules.`android-library`
}

dependencies {
    implementation(libs.androidx.test.runner)
    implementation(libs.hilt.android)
    implementation(libs.hilt.test)
    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)
}
