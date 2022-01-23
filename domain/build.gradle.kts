plugins {
    id("modules.kotlin-library")
}

dependencies {
    implementation(libs.bundles.common)

    testImplementation(libs.bundles.test)
}
