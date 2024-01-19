plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)

        implementation(projects.ui.base)
        implementation(projects.ui.account)
        implementation(projects.ui.explore)
        implementation(projects.ui.lists)
        implementation(projects.ui.social)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
