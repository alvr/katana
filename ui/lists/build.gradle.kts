plugins {
    id("katana.feature.ui")
    id("kotlin-parcelize")
    alias(libs.plugins.parcelize)
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.lists)
    implementation(projects.ui.base)

    implementation(libs.parcelable)

    testImplementation(projects.common.tests)
}
