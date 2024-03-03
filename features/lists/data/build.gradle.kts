plugins {
    id("katana.multiplatform.data.remote")
}

dependencies {
    apolloMetadata(projects.core.remote)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.user.domain)

        implementation(projects.core.common)
        implementation(projects.core.remote)

        implementation(projects.features.lists.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
