plugins {
    id("katana.multiplatform.ui")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
        implementation(libs.compose.placeholder)
    }

    androidMainDependencies {
        implementation(libs.accompanist.systemuicontroller)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
