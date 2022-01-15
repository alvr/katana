plugins {
    plugins.dependencies
    plugins.detekt
}

buildscript {
    extra.set("detektFormatting", libs.detekt.formatting)
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
