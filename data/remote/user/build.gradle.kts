import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
}

android.namespace = "${KatanaConfiguration.PackageName}.data.remote.user"

apollo {
    generateAsInternal.set(true)
    generateTestBuilders.set(true)
    packageName.set("dev.alvr.katana.data.remote.user")
}

dependencies {
    apolloMetadata(projects.data.remote.base)
    implementation(projects.common.core)
    implementation(projects.data.remote.base)
    implementation(projects.domain.user)

    implementation(libs.bundles.data.remote)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test.data.remote)
}
