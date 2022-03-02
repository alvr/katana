package modules

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import utils.configureKotlin

plugins {
    kotlin
    `kotlin-kapt`
}

kapt.correctErrorTypes = true

java {
    sourceCompatibility = KatanaConfiguration.UseJavaVersion
    targetCompatibility = KatanaConfiguration.UseJavaVersion
}

tasks {
    test {
        useJUnitPlatform()
    }

    withType<KotlinCompile> {
        kotlinOptions.configureKotlin()
    }
}
