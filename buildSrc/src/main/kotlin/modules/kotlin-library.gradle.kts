package modules

plugins {
    kotlin
}

java {
    sourceCompatibility = KatanaConfiguration.UseJavaVersion
    targetCompatibility = KatanaConfiguration.UseJavaVersion
}

tasks.test {
    useJUnitPlatform()
}
