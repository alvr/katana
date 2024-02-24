plugins {
    id("katana.feature.data.remote")
}

dependencies {
    apolloMetadata(projects.data.remote.base)

    implementation(projects.data.remote.base)
    implementation(projects.domain.explore)

    testImplementation(projects.common.tests)
}
