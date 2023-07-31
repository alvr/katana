plugins {
    id("katana.multiplatform.compose")
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
        implementation(projects.domain.lists)
        implementation(projects.ui.base)
    }

    commonTestDependencies {
        implementation(projects.common.tests)
    }
}
