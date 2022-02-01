package modules

plugins {
    kotlin
    `kotlin-kapt`
}

kapt.correctErrorTypes = true

java {
    sourceCompatibility = KatanaConfiguration.UseJavaVersion
    targetCompatibility = KatanaConfiguration.UseJavaVersion
}

tasks.test {
    useJUnitPlatform()
}
