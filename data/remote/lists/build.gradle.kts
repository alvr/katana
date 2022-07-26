plugins {
    alias(libs.plugins.apollo)
    modules.`android-library`
}

apollo {
    generateAsInternal.set(true)
    generateTestBuilders.set(true)
    packageName.set("dev.alvr.katana.data.remote.lists")
}

android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

dependencies {
    apolloMetadata(projects.data.remote.base)
    implementation(projects.data.remote.base)
    implementation(projects.domain.lists)
    implementation(projects.domain.user)

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    kapt(libs.bundles.kapt)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
    testImplementation(libs.bundles.test.data.remote)
}
