plugins {
    id("katana.multiplatform.data.remote")
}

dependencies {
    apolloMetadata(projects.data.remote.base)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.data.remote.base)
        implementation(projects.domain.lists)
        implementation(projects.domain.user)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
