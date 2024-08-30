plugins {
    `kotlin-dsl`
    kotlin("plugin.serialization")
}

group = "dev.alvr.katana.buildlogic"
version = extra["katana.plugins.version"].toString()

dependencies {
    implementation(libs.bundles.gradle)
    implementation(libs.kotlinpoet)
    implementation(libs.kaml)
}

gradlePlugin {
    plugins {
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
        register("multiplatform-app") {
            id = "katana.multiplatform.app"
            implementationClass = "dev.alvr.katana.buildlogic.mp.app.KatanaMultiplatformAppPlugin"
        }
        register("multiplatform-compose") {
            id = "katana.multiplatform.compose"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.ui.KatanaMultiplatformComposePlugin"
        }
        register("multiplatform-core") {
            id = "katana.multiplatform.core"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformCorePlugin"
        }
        register("multiplatform-data-preferences") {
            id = "katana.multiplatform.data.preferences"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.data.KatanaMultiplatformDataPreferencesPlugin"
        }
        register("multiplatform-data-remote") {
            id = "katana.multiplatform.data.remote"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.data.KatanaMultiplatformDataRemotePlugin"
        }
        register("multiplatform-mobile") {
            id = "katana.multiplatform.mobile"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.KatanaMultiplatformMobilePlugin"
        }
        register("multiplatform-tests") {
            id = "katana.multiplatform.tests"
            implementationClass = "dev.alvr.katana.buildlogic.mp.core.KatanaMultiplatformTestsPlugin"
        }
        register("multiplatform-ui") {
            id = "katana.multiplatform.ui"
            implementationClass = "dev.alvr.katana.buildlogic.mp.mobile.ui.KatanaMultiplatformUiPlugin"
        }
    }
}
