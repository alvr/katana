plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.kotlin"

dependencies {
    implementation(libs.gradle.kotlin)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("kotlin-test") {
            id = "katana.kotlin.test"
            implementationClass = "dev.alvr.katana.buildlogic.kotlin.KotlinTestLibraryConventionPlugin"
        }
    }
}
