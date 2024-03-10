plugins {
    id("katana.multiplatform.data.preferences")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.core.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
