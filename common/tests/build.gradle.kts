plugins {
    id("katana.multiplatform.tests")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.arrow)
    }
}
