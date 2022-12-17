import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.compose.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "${KatanaConfiguration.PackageName}.ui.lists"
}

ksp {
    arg("compose-destinations.mode", "destinations")
    arg("compose-destinations.moduleName", "lists")
}

dependencies {
    implementation(projects.common.core)
    implementation(projects.domain.lists)
    implementation(projects.ui.base)

    testImplementation(projects.common.testsAndroid)
}
