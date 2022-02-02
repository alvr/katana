plugins {
    id("modules.kotlin-library")
}

dependencies {
    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)
}
