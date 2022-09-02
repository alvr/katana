plugins {
    id("katana.common")
    id("katana.dependency-versions")
    id("katana.detekt")
    id("katana.kover")
    id("katana.sonarqube")
    alias(libs.plugins.hilt) apply false
}
