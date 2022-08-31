plugins {
    modules.`android-library`
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.base)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.preferences)

    kapt(libs.bundles.kapt)

    testImplementation(projects.common.testsAndroid)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.android)
    kaptTest(libs.bundles.kapt)
}
