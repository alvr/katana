plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.core.common)
        implementation(libs.compose.placeholder)
        implementation(libs.materialkolor)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
