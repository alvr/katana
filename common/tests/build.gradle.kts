plugins {
    id("katana.multiplatform.tests")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.arrow)
        implementation(libs.koin)
        implementation(libs.koin.test.get().toString()) { exclude(group = "junit", module = "junit") }
    }
}
