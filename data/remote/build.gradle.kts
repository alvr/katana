plugins {
    id("modules.android-library")
    alias(libs.plugins.apollo)
}

apollo {
    generateAsInternal.set(true)
    generateTestBuilders.set(true)
    packageNamesFromFilePaths("dev.alvr.katana.data.remote.gql")

    introspection {
        endpointUrl.set("https://graphql.anilist.co")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
    }
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.remote)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
