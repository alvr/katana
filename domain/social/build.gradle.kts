plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        api(projects.domain.base)
    }

    commonTestDependencies { implementation(projects.common.tests) }
}
