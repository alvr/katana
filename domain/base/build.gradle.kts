plugins {
    id("modules.kotlin-library")
}

dependencies {
    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)
}
