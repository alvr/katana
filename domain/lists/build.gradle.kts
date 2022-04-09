plugins {
    id("modules.kotlin-library")
}

dependencies {
    api(projects.domain.base)

    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)
}
