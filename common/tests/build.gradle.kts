plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.arrow)
        implementation(libs.bundles.test)
    }

    jvmMainDependencies { implementation(libs.bundles.test.jvm) }
}
