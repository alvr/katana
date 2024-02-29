plugins {
    id("katana.multiplatform.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "explore")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.features.explore.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
