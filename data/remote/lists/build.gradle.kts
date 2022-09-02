plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
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
    implementation(projects.common.core)
    implementation(projects.data.remote.base)
    implementation(projects.domain.lists)
    implementation(projects.domain.user)

    coreLibraryDesugaring(libs.desugaring)

    implementation(libs.bundles.data.remote)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test.data.remote)
}
