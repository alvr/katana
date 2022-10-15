plugins {
    id("katana.kotlin")
}

dependencies {
    implementation(projects.common.core)
    api(projects.domain.base)

    testImplementation(projects.common.tests)
}
