plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.core.ui)

        implementation(projects.features.account.ui)
        implementation(projects.features.explore.ui)
        implementation(projects.features.lists.ui)
        implementation(projects.features.social.ui)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
