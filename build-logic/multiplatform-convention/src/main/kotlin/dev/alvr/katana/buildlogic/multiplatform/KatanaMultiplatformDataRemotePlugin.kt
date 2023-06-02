package dev.alvr.katana.buildlogic.multiplatform

import com.apollographql.apollo3.gradle.api.ApolloExtension
import dev.alvr.katana.buildlogic.ConventionPlugin
import dev.alvr.katana.buildlogic.catalogBundle
import dev.alvr.katana.buildlogic.fullPackageName
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.getting
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

internal class KatanaMultiplatformDataRemotePlugin : ConventionPlugin {
    override fun Project.configure() {
        apply(plugin = "katana.multiplatform.mobile")
        apply(plugin = "com.apollographql.apollo3")

        with(extensions) {
            configure<KotlinMultiplatformExtension> { configureSourceSets() }
            configure<ApolloExtension> { configureApollo(project) }
        }
    }

    @Suppress("UNUSED_VARIABLE")
    private fun KotlinMultiplatformExtension.configureSourceSets() {
        configureSourceSets {
            val commonMain by getting {
                dependencies {
                    implementation(catalogBundle("data-remote-common"))
                }
            }
            val androidMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("data-remote-android"))
                }
            }
            val iosMain by getting {
                dependsOn(commonMain)
                dependencies {
                    implementation(catalogBundle("data-remote-ios"))
                }
            }
            val iosSimulatorArm64Main by getting { dependsOn(iosMain) }

            val commonTest by getting {
                dependencies {
                    implementation(catalogBundle("data-remote-common-test"))
                }
            }
            val androidUnitTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("data-remote-android-test"))
                }
            }
            val iosTest by getting {
                dependsOn(commonTest)
                dependencies {
                    implementation(catalogBundle("data-remote-ios-test"))
                }
            }
            val iosSimulatorArm64Test by getting { dependsOn(iosTest) }
        }
    }

    private fun ApolloExtension.configureApollo(project: Project) {
        val namespace = project.fullPackageName

        service("anilist") {
            generateAsInternal.set(true)
            generateDataBuilders.set(true)
            packageName.set(namespace)

            if (namespace.contains(BASE_PACKAGE)) {
                alwaysGenerateTypesMatching.set(listOf("Query", "User"))
                generateApolloMetadata.set(true)
                generateAsInternal.set(false)

                introspection {
                    endpointUrl.set("https://graphql.anilist.co")
                    schemaFile.set(project.file("src/commonMain/graphql/schema.graphqls"))
                }
            }
        }
    }

    private companion object {
        const val BASE_PACKAGE = ".base"
    }
}
