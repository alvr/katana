import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
}

android {
    namespace = "${KatanaConfiguration.PackageName}.data.remote.lists"
    compileOptions.isCoreLibraryDesugaringEnabled = true
}

apollo {
    generateAsInternal.set(true)
    generateTestBuilders.set(true)
    packageName.set("dev.alvr.katana.data.remote.lists")
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
