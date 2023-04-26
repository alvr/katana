plugins {
    id("katana.multiplatform.data.remote")
}

dependencies {
    apolloMetadata(projects.data.remote.base)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.data.remote.base)
        implementation(projects.domain.social)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
