plugins {
    id("modules.android-library")
    `kotlinx-serialization`
}

dependencies {
    implementation(projects.data.preferences.base)
    implementation(projects.domain.user)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
