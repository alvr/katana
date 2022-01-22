plugins {
    id("modules.android-library")
    alias(libs.plugins.ksp)
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
    arg("room.expandProjection", "true")
}

dependencies {
    implementation(libs.bundles.common.android)
    implementation(libs.bundles.data.db)

    ksp(libs.room.compiler)

    testImplementation(libs.bundles.test)

    androidTestImplementation(libs.bundles.test.android)
}
