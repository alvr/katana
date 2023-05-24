plugins {
    id("katana.multiplatform.compose")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "explore")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.domain.explore)
        implementation(projects.ui.base)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
