plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    compileSdk = KatanaConfiguration.CompileSdk
    buildToolsVersion = KatanaConfiguration.BuildTools

    defaultConfig {
        applicationId = KatanaConfiguration.PackageName
        minSdk = KatanaConfiguration.MinSdk
        targetSdk = KatanaConfiguration.TargetSdk
        versionCode = KatanaConfiguration.VersionCode
        versionName = KatanaConfiguration.VersionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
    }

    buildTypes {
        debug {
            isDebuggable = true
            isDefault = true
            isMinifyEnabled = false
            isTestCoverageEnabled = false
        }

        release {
            isDebuggable = false
            isDefault = false
            isMinifyEnabled = true
            isTestCoverageEnabled = false

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = KatanaConfiguration.UseJavaVersion
        targetCompatibility = KatanaConfiguration.UseJavaVersion
    }

    kotlinOptions {
        jvmTarget = KatanaConfiguration.JvmTarget
        apiVersion = KatanaConfiguration.KotlinVersion
        languageVersion = KatanaConfiguration.KotlinVersion
    }

    buildFeatures.compose = true
    composeOptions.kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()

    packagingOptions {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {
    implementation(libs.androidx.activity)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core)

    implementation(libs.compose.material)
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.preview)
    debugImplementation(libs.compose.ui.tooling)

    implementation(libs.design.material)

    testImplementation(libs.junit)

    androidTestImplementation(libs.androidx.test.junit)
    androidTestImplementation(libs.espresso)
    androidTestImplementation(libs.compose.ui.test)
}
