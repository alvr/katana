plugins {
    id("katana.app")
}

dependencies {
    coreLibraryDesugaring(libs.desugaring)

    implementation(projects.core.ui)
    implementation(projects.shared)

    implementation(libs.bundles.app.android)
}
