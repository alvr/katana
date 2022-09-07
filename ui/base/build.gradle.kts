plugins {
    id("katana.android.compose.library")
}

dependencies {
    implementation(projects.common.core)

    implementation(libs.accompanist.systemuicontroller)

    testImplementation(projects.common.tests)
}
