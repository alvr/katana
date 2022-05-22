plugins {
    modules.`kotlin-library`
}

dependencies {
    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    implementation(libs.bundles.test)
}
