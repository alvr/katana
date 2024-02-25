plugins {
    id("katana.core")
}

dependencies {
    commonMainImplementation(projects.common.core)
    commonMainApi(projects.domain.base)

    commonTestImplementation(projects.common.tests)
}
