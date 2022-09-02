plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
}

android {
    buildFeatures.buildConfig = true
}

apollo {
    alwaysGenerateTypesMatching.set(listOf("Query", "User"))
    generateApolloMetadata.set(true)
    packageName.set("dev.alvr.katana.data.remote.base")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)

    implementation(libs.bundles.data.remote)

    implementation(libs.apollo.cache.sql)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)

    testImplementation(projects.common.tests)
}
