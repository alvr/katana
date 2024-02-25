plugins {
    id("katana.common")
    id("katana.detekt")
    id("katana.kover")

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.apollo) apply false
    alias(libs.plugins.buildconfig) apply false
    alias(libs.plugins.complete.kotlin) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.detekt) apply false
    alias(libs.plugins.kotest) apply false
    alias(libs.plugins.kotlin) apply false
    alias(libs.plugins.kover) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.mokkery) apply false
    alias(libs.plugins.sentry) apply false
}
