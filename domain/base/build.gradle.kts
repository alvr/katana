plugins {
    id("katana.core")
}

dependencies {
    commonMainImplementation(projects.common.core)
    commonTestImplementation(projects.common.tests)
}
