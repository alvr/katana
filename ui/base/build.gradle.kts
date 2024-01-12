plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(libs.compose.placeholder)
        implementation(libs.materialkolor)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
