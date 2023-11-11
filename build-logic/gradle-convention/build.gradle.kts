plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.gradle"
version = extra["katana.plugins.version"].toString()

dependencies {
    compileOnly(libs.gradle.complete.kotlin)
    compileOnly(libs.gradle.kotlin)
    compileOnly(libs.gradle.kover)
    compileOnly(libs.gradle.sentry)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("common") {
            id = "katana.common"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KatanaCommonPlugin"
        }
        register("kover") {
            id = "katana.kover"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KatanaKoverPlugin"
        }
        register("sentry") {
            id = "katana.sentry"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KatanaSentryPlugin"
        }
    }
}
