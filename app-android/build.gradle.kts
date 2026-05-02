plugins { id("katana.app") }

dependencies {
    baselineProfile(projects.macrobenchmark)

    coreLibraryDesugaring(libs.desugaring)

    implementation(projects.core.ui)
    implementation(projects.shared)

    implementation(libs.bundles.app.android)
}
