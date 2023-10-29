plugins {
    id("katana.multiplatform.compose")
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
