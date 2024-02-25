plugins {
    id("katana.feature.ui")
}

dependencies {
    implementation(projects.common.core)
    implementation(libs.compose.placeholder)
    implementation(libs.materialkolor)

    testImplementation(projects.common.tests)
}
