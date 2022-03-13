plugins {
    id("modules.android-library")
    alias(libs.plugins.apollo)
}

apollo {
    generateApolloMetadata.set(true)
    packageNamesFromFilePaths("dev.alvr.katana.data.remote.user")
}

dependencies {
    apolloMetadata(projects.data.remote.base)
    implementation(projects.data.remote.base)
    implementation(projects.domain.user)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
