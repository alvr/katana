plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.core.ui)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
