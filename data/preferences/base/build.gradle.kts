plugins {
    id("katana.feature.data.preferences")
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.base)

    testImplementation(projects.common.tests)
}
