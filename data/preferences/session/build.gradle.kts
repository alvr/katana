plugins {
    id("katana.feature.data.preferences")
    alias(libs.plugins.serialization)
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.data.preferences.base)
    implementation(projects.domain.session)

    testImplementation(projects.common.tests)
}
