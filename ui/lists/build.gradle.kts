plugins {
    modules.`compose-library`
}

android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.lists)
    implementation(projects.ui.base)

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.ui.compose)

    kapt(libs.bundles.kapt.ui)
    ksp(libs.bundles.ksp.ui)

    debugImplementation(libs.compose.ui.test.manifest)

    testImplementation(projects.common.testsAndroid)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.android)
}
