plugins {
    id("katana.common")
    id("katana.dependency-versions")
    id("katana.detekt")
    id("katana.kover")
    id("katana.sonar")
    alias(libs.plugins.compose) apply false
}
