plugins {
    id("katana.multiplatform.data.preferences")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(projects.core.preferences)
        implementation(projects.common.session.domain)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
