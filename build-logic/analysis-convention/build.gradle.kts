plugins {
    `kotlin-dsl`
}

group = "dev.alvr.katana.buildlogic.analysis"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.gradle.detekt)
    implementation(libs.gradle.sonarqube)
    implementation(project(":common"))
}

gradlePlugin {
    plugins {
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaDetektPlugin"
        }
        register("sonar") {
            id = "katana.sonar"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaSonarPlugin"
        }
        register("sonar-mobile") {
            id = "katana.sonar.mobile"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaSonarMobilePlugin"
        }
        register("sonar-kotlin") {
            id = "katana.sonar.kotlin"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaSonarKotlinPlugin"
        }
    }
}
