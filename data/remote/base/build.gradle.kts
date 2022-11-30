import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.library")
    alias(libs.plugins.apollo)
}

val pkg = "${KatanaConfiguration.PackageName}.data.remote.base"

android {
    namespace = pkg
    buildFeatures.buildConfig = true
}

apollo {
    alwaysGenerateTypesMatching.set(listOf("Query", "User"))
    generateApolloMetadata.set(true)
    packageName.set(pkg)

    introspection {
        endpointUrl.set("https://graphql.anilist.co")
        schemaFile.set(file("src/main/graphql/schema.graphqls"))
    }
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)
    implementation(libs.bundles.data.remote)
    implementation(libs.apollo.cache.sql)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logger)
    implementation(libs.sentry.apollo)

    testImplementation(projects.common.tests)
}
