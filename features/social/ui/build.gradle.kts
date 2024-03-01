plugins {
    id("katana.multiplatform.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "social")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.core.ui)

        implementation(projects.features.social.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
