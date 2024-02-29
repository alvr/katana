plugins {
    id("katana.multiplatform.data.remote")
}

dependencies {
    apolloMetadata(projects.core.remote)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.remote)
        implementation(projects.features.explore.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
