plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
}

group = "dev.alvr.katana.buildlogic"

version = extra["katana.plugins.version"].toString()

kotlin.compilerOptions.freeCompilerArgs.addAll("-Xcontext-parameters")

dependencies {
    implementation(libs.bundles.build.config)
    implementation(libs.bundles.gradle)
}

gradlePlugin {
    plugins {
        register("app") {
            id = "katana.app"
            implementationClass = "dev.alvr.katana.buildlogic.mp.KatanaAppPlugin"
        }
        register("baselineprofile") {
            id = "katana.baselineprofile"
            implementationClass = "dev.alvr.katana.buildlogic.mp.KatanaBaselineProfilePlugin"
        }
        register("common") {
            id = "katana.common"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaCommonPlugin"
        }
        register("build-config") {
            id = "katana.build-config"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaBuildConfigPlugin"
        }
        register("detekt") {
            id = "katana.detekt"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaDetektPlugin"
        }
        register("kover") {
            id = "katana.kover"
            implementationClass = "dev.alvr.katana.buildlogic.common.KatanaKoverPlugin"
        }
        register("macrobenchmark") {
            id = "katana.macrobenchmark"
            implementationClass = "dev.alvr.katana.buildlogic.mp.KatanaMacrobenchmarkPlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.mp.ui.KatanaMultiplatformComposePlugin"
        }
        register("multiplatform-core") {
            id = "katana.multiplatform.core"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformCorePlugin"
        }
        register("multiplatform-data-preferences") {
            id = "katana.multiplatform.data.preferences"
            implementationClass = "dev.alvr.katana.buildlogic.mp.data.KatanaMultiplatformDataPreferencesPlugin"
        }
        register("multiplatform-data-remote") {
            id = "katana.multiplatform.data.remote"
            implementationClass = "dev.alvr.katana.buildlogic.mp.data.KatanaMultiplatformDataRemotePlugin"
        }
        register("multiplatform-tests") {
            id = "katana.multiplatform.tests"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformTestsPlugin"
        }
        register("multiplatform-ui") {
            id = "katana.multiplatform.ui"
            implementationClass = "dev.alvr.katana.buildlogic.mp.ui.KatanaMultiplatformUiPlugin"
        }
        register("spotless") {
            id = "katana.spotless"
            implementationClass = "dev.alvr.katana.buildlogic.analysis.KatanaSpotlessPlugin"
        }
    }
}
