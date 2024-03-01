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
        implementation(projects.core.common)
        implementation(projects.core.ui)

        implementation(projects.features.lists.domain)

        implementation(libs.parcelable)
    }

    commonTestDependencies {
        implementation(projects.core.tests)
    }
}
