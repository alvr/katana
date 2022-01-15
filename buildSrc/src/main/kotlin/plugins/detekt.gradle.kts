package plugins

import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektPlugin

apply<DetektPlugin>()

val detektFormatting: Any by rootProject.extra

tasks.register<Detekt>("detektAll") {
    description = "Run detekt in all modules"

    parallel = true
    ignoreFailures = false
    autoCorrect = true
    buildUponDefaultConfig = true
    setSource(files(projectDir))
    config.setFrom(files("$rootDir/config/detekt.yml"))
    include("**/*.kt", "**/*.kts")
    exclude("**/resources/**", "**/build/**")

    reports {
        html.required.set(true)
        sarif.required.set(true)
        txt.required.set(false)
        xml.required.set(false)
    }
}

dependencies {
    add("detektPlugins", detektFormatting)
}
