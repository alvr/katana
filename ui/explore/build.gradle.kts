plugins {
    id("katana.android.compose.library")
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "explore")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
