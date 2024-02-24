plugins {
    id("katana.feature.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "social")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.social)
    implementation(projects.ui.base)

    testImplementation(projects.common.tests)
}
