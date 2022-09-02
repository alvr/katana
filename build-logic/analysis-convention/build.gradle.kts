plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.analysis"

dependencies {
    implementation(libs.gradle.detekt)
    implementation(libs.gradle.sonarqube)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.DetektConventionPlugin"
        }
        register("sonarqube") {
            id = "katana.sonarqube"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarQubeConventionPlugin"
        }
        register("sonarqube-android") {
            id = "katana.sonarqube.android"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarQubeAndroidConventionPlugin"
        }
        register("sonarqube-kotlin") {
            id = "katana.sonarqube.kotlin"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.SonarQubeKotlinConventionPlugin"
        }
    }
}
