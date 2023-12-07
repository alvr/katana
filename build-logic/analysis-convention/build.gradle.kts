plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.analysis"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.detekt)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaDetektPlugin"
        }
    }
}
