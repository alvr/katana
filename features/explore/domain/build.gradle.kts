plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        api(projects.core.domain)
    }

    commonTestDependencies { implementation(projects.core.tests) }
}
