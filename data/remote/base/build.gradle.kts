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
    implementation(projects.common.core)
    implementation(projects.domain.session)

    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    implementation(libs.apollo.cache.sql)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)

    kapt(libs.bundles.kapt)

    testImplementation(projects.common.tests)
    testImplementation(libs.bundles.test)
}
