plugins {
    id("katana.multiplatform.core")
}

katanaMultiplatform {
    commonMainDependencies { implementation(projects.common.core) }
    commonTestDependencies { implementation(projects.common.tests) }
}
