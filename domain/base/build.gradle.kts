plugins {
    modules.`kotlin-library`
}

dependencies {
    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
}
