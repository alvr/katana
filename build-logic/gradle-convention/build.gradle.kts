plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.gradle"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.complete.kotlin)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.kover)
    implementation(libs.gradle.sentry)
    implementation(libs.gradle.updates)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("common") {
            id = "katana.common"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KatanaCommonPlugin"
        }
        register("dependency-versions") {
            id = "katana.dependency-versions"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KatanaDependencyVersionsPlugin"
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
