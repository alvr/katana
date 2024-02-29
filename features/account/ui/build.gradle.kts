plugins {
    id("katana.multiplatform.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "account")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.common.session.domain)
        implementation(projects.common.user.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
