plugins {
    id("katana.android.test.library")
}

dependencies {
    api(projects.common.tests)

    implementation(libs.bundles.test.android)
}
