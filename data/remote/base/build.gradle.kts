plugins {
    id("modules.android-library")
    alias(libs.plugins.apollo)
}

android {
    buildFeatures.buildConfig = true
}

apollo {
    generateApolloMetadata.set(true)
    packageNamesFromFilePaths("dev.alvr.katana.data.remote.base")
}

dependencies {
    implementation(projects.domain.token)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    implementation(libs.apollo.cache.sql)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)

    kapt(libs.bundles.kapt)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
