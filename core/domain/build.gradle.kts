plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies { implementation(projects.core.common) }
    commonTestDependencies { implementation(projects.core.tests) }
}
