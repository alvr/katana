plugins {
    id("katana.multiplatform.data.remote")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.remote)

        implementation(projects.features.account.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
