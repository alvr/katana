plugins {
    modules.`kotlin-library`
}

dependencies {
    implementation(projects.common.core)
    api(projects.domain.base)

    implementation(libs.bundles.common)

    kapt(libs.bundles.kapt)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
}
