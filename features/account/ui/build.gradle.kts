plugins {
    id("katana.multiplatform.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "account")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.session.domain)
        implementation(projects.common.user.domain)

        implementation(projects.core.common)
        implementation(projects.core.ui)

        implementation(projects.features.account.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
