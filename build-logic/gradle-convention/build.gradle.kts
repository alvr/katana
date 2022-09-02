plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.gradle"

dependencies {
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
            implementationClass = "dev.alvr.katana.buildlogic.gradle.CommonConventionPlugin"
        }
        register("dependency-versions") {
            id = "katana.dependency-versions"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.DependencyVersionsConventionPlugin"
        }
        register("kover") {
            id = "katana.kover"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.KoverConventionPlugin"
        }
        register("sentry") {
            id = "katana.sentry"
            implementationClass = "dev.alvr.katana.buildlogic.gradle.SentryConventionPlugin"
        }
    }
}
