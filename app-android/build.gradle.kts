plugins {
    id("katana.multiplatform.app")
}

katanaMultiplatform {
    commonMainDependencies {
        implementation(projects.shared)
    }
}

dependencies {
    coreLibraryDesugaring(libs.desugaring)
}
