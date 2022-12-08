import dev.alvr.katana.buildlogic.KatanaConfiguration

plugins {
    id("katana.android.compose.library")
    id("org.jetbrains.kotlin.plugin.parcelize")
}

android {
    namespace = "${KatanaConfiguration.PackageName}.ui.base"
    compileOptions.isCoreLibraryDesugaringEnabled = true
}

dependencies {
    implementation(projects.common.core)

    implementation(libs.accompanist.systemuicontroller)
    implementation(libs.design.calendar)

    coreLibraryDesugaring(libs.desugaring)

    testImplementation(projects.common.tests)
}
