plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.android"

dependencies {
    implementation(libs.gradle.android)
    implementation(libs.gradle.kotlin)
    implementation(libs.gradle.ksp)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("android-application") {
            id = "katana.android.application"
            implementationClass = "dev.alvr.katana.buildlogic.android.AndroidApplicationConventionPlugin"
        }
        register("android-library") {
            id = "katana.android.library"
            implementationClass = "dev.alvr.katana.buildlogic.android.AndroidLibraryConventionPlugin"
        }
        register("android-compose-library") {
            id = "katana.android.compose.library"
            implementationClass = "dev.alvr.katana.buildlogic.android.AndroidComposeLibraryConventionPlugin"
        }
        register("android-test-library") {
            id = "katana.android.test.library"
            implementationClass = "dev.alvr.katana.buildlogic.android.AndroidTestLibraryConventionPlugin"
        }
    }
}
