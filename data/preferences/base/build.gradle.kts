plugins {
    id("katana.multiplatform.data.preferences")
    alias(libs.plugins.serialization)
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(projects.domain.base)
    }

    androidUnitTestDependencies {
        implementation(projects.common.tests)
    }
}
