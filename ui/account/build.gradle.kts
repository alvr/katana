plugins {
    id("katana.feature.ui")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "account")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)
    implementation(projects.domain.user)
    implementation(projects.ui.base)

    testImplementation(projects.common.tests)
}
