plugins {
    id("katana.android.compose.library")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "login")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.session)
    implementation(projects.domain.user)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
