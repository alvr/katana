plugins { id("katana.macrobenchmark") }

dependencies {
    implementation(libs.androidx.benchmark.macro.junit4)
    implementation(libs.androidx.test.ext.junit)
}

androidComponents {
    onVariants(selector().withBuildType("benchmark")) { variant ->
        variant.instrumentationRunnerArguments.put("androidx.benchmark.enabledRules", "Macrobenchmark")
    }
}

tasks.register("baseline") {
    group = "verification"
    description = "Generate and copy release baseline profile text artifacts into app sources."
    dependsOn(":app-android:generateBetaBaselineProfile", ":app-android:generateReleaseBaselineProfile")
}

tasks.register("benchmark") {
    group = "verification"
    description = "Run macrobenchmark instrumentation tests with Macrobenchmark benchmark rule only."
    dependsOn(":macrobenchmark:connectedBenchmarkReleaseAndroidTest")
}
