plugins {
    id("katana.multiplatform.ui")
    id("kotlin-parcelize")
    alias(libs.plugins.parcelize)
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(libs.parcelable)

        implementation(projects.common.core)
        implementation(projects.features.lists.domain)
        implementation(projects.ui.base)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
