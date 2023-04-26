plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.arrow)
        implementation(libs.bundles.core.common.test)
    }

    jvmMainDependencies { implementation(libs.bundles.core.jvm.test) }
}
