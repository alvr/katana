plugins {
    id("katana.android.compose.library")
}

dependencies {
    implementation(projects.common.core)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.compose.fonts)

    testImplementation(projects.common.tests)
}
