plugins {
    id("katana.multiplatform.compose")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.common.core)
    }

    androidMainDependencies {
        implementation(libs.accompanist.systemuicontroller)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
