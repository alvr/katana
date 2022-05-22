plugins {
    alias(libs.plugins.apollo)
    modules.`android-library`
}

android {
    buildFeatures.buildConfig = true
}

apollo {
    alwaysGenerateTypesMatching.set(listOf("Query"))
    generateApolloMetadata.set(true)
    packageName.set("dev.alvr.katana.data.remote.base")
}

dependencies {
    implementation(projects.domain.token)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    implementation(libs.apollo.cache.sql)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)

    kapt(libs.bundles.kapt)

    testImplementation(projects.utils.tests.unit)
    testImplementation(libs.bundles.test)

    androidTestImplementation(projects.utils.tests.android)
    androidTestImplementation(libs.bundles.test.android)
    kaptAndroidTest(libs.bundles.kapt)
}
