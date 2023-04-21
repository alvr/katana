plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.arrow)
        implementation(libs.bundles.common.test)
    }

    jvmMainDependencies { implementation(libs.bundles.common.test.jvm) }
}
