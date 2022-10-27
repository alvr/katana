plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.analysis"

dependencies {
    implementation(libs.gradle.detekt)
    implementation(libs.gradle.sonar)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.DetektConventionPlugin"
        }
        register("sonar") {
            id = "katana.sonar"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarConventionPlugin"
        }
        register("sonar-android") {
            id = "katana.sonar.android"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarAndroidConventionPlugin"
        }
        register("sonar-kotlin") {
            id = "katana.sonar.kotlin"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarKotlinConventionPlugin"
        }
    }
}
