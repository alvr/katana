plugins {
    id("modules.android-library")
}

dependencies {
    implementation(libs.bundles.common.android)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.common.android)
}
