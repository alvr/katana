plugins {
    id("katana.feature.data.remote")
}

dependencies {
    apolloMetadata(projects.data.remote.base)

    implementation(projects.common.core)
    implementation(projects.data.remote.base)
    implementation(projects.domain.lists)
    implementation(projects.domain.user)

    testImplementation(projects.common.tests)
}
