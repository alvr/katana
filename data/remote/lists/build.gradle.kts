import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
}

val pkg = "${KatanaConfiguration.PackageName}.data.remote.lists"

android {
    namespace = pkg
    compileOptions.isCoreLibraryDesugaringEnabled = true
}

apollo {
    generateAsInternal.set(true)
    generateTestBuilders.set(true)
    packageName.set(pkg)
}

dependencies {
    apolloMetadata(projects.data.remote.base)

    implementation(projects.common.core)
    implementation(projects.data.remote.base)
    implementation(projects.domain.lists)
    implementation(projects.domain.user)
    implementation(libs.bundles.data.remote)

    coreLibraryDesugaring(libs.desugaring)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test.data.remote)
}
