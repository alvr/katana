plugins {
    id("katana.multiplatform.app")
}

dependencies {
    implementation(projects.common.core)

    implementation(projects.data.preferences.base)
    implementation(projects.data.preferences.session)

    implementation(projects.data.remote.account)
    implementation(projects.data.remote.base)
    implementation(projects.data.remote.explore)
    implementation(projects.data.remote.lists)
    implementation(projects.data.remote.social)
    implementation(projects.data.remote.user)

    implementation(projects.domain.account)
    implementation(projects.domain.base)
    implementation(projects.domain.explore)
    implementation(projects.domain.lists)
    implementation(projects.domain.session)
    implementation(projects.domain.social)
    implementation(projects.domain.user)

    implementation(projects.ui.account)
    implementation(projects.ui.base)
    implementation(projects.ui.explore)
    implementation(projects.ui.lists)
    implementation(projects.ui.login)
    implementation(projects.ui.main)
    implementation(projects.ui.social)

    testImplementation(projects.common.tests)
}

// kotlin {
//    androidTarget {
//        compilations.all {
//            kotlinOptions {
//                jvmTarget = "1.8"
//            }
//        }
//    }
//
//    listOf(
//        iosX64(),
//        iosArm64(),
//        iosSimulatorArm64(),
//    ).forEach { iosTarget ->
//        iosTarget.binaries.framework {
//            baseName = "ComposeApp"
//            isStatic = true
//        }
//    }
//
//    sourceSets {
//
//        androidMain.dependencies {
//            implementation(libs.compose.ui)
//            implementation(libs.compose.ui.tooling.preview)
//            implementation(libs.androidx.activity.compose)
//        }
//        commonMain.dependencies {
//            implementation(compose.runtime)
//            implementation(compose.foundation)
//            implementation(compose.material)
//            @OptIn(ExperimentalComposeLibrary::class)
//            implementation(compose.components.resources)
//        }
//    }
// }
//
// android {
//    namespace = "dev.alvr.katana"
//    compileSdk = libs.versions.android.compileSdk.get().toInt()
//
//    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
//    sourceSets["main"].res.srcDirs("src/androidMain/res")
//    sourceSets["main"].resources.srcDirs("src/commonMain/resources")
//
//    defaultConfig {
//        applicationId = "dev.alvr.katana"
//        minSdk = libs.versions.android.minSdk.get().toInt()
//        targetSdk = libs.versions.android.targetSdk.get().toInt()
//        versionCode = 1
//        versionName = "1.0"
//    }
//    buildFeatures {
//        compose = true
//    }
//    composeOptions {
//        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
//    }
//    packaging {
//        resources {
//            excludes += "/META-INF/{AL2.0,LGPL2.1}"
//        }
//    }
//    buildTypes {
//        getByName("release") {
//            isMinifyEnabled = false
//        }
//    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_1_8
//        targetCompatibility = JavaVersion.VERSION_1_8
//    }
//    dependencies {
//        debugImplementation(libs.compose.ui.tooling)
//    }
// }
