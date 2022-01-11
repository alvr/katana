buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
    }
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
