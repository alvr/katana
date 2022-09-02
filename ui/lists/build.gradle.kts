plugins {
    id("katana.android.compose.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
    }
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.lists)
    implementation(projects.ui.base)

    coreLibraryDesugaring(libs.desugaring)

    testImplementation(projects.common.testsAndroid)
}
