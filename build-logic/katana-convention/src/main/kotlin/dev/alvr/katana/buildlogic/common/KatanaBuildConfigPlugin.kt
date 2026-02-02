package dev.alvr.katana.buildlogic.common

import com.charleskorn.kaml.Yaml
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import dev.alvr.katana.buildlogic.common.Config.Environment.BuildConfig
import dev.alvr.katana.buildlogic.fullPackageName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.file.Directory
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.MapProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectories
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.assign
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getValue
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.provideDelegate
import org.gradle.kotlin.dsl.registering
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jetbrains.kotlin.gradle.tasks.KotlinNativeLink

internal class KatanaBuildConfigPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        val sourceSets = extensions.getByType<KotlinMultiplatformExtension>().sourceSets
        val sourceOutputDirs = OutputDir.entries.associateWith { output ->
            project.outputDir(output.sourceSet).get()
        }

        val generateBuildConfig by tasks.registering(GenerateBuildConfigTask::class) {
            config = rootProject.file("gradle/config/build_config.yml")
            flavor = providers.gradleProperty("katana.flavor").getOrElse("dev")
            packageName = fullPackageName
            outputDirs = sourceOutputDirs
        }

        tasks {
            withType<KotlinCompile> { dependsOn(generateBuildConfig) }
            withType<KotlinNativeLink> { dependsOn(generateBuildConfig) }
        }

        configure<KotlinMultiplatformExtension> {
            sourceSets {
                sourceOutputDirs.forEach { (output, dir) ->
                    val sourceSet = getByName(output.sourceSet)
                    sourceSet.kotlin.srcDir(dir)
                }
            }
        }
    }

    private fun Project.outputDir(name: String) = layout.buildDirectory.dir("$GENERATED_DIR/$name")
}

@CacheableTask
internal abstract class GenerateBuildConfigTask : DefaultTask() {
    @get:InputFile
    @get:PathSensitive(PathSensitivity.ABSOLUTE)
    abstract val config: RegularFileProperty

    @get:Input
    abstract val flavor: Property<String>

    @get:Input
    abstract val packageName: Property<String>

    @get:OutputDirectories
    abstract val outputDirs: MapProperty<OutputDir, Directory>

    @TaskAction
    fun generate() {
        val config = Yaml.default.decodeFromString(Config.serializer(), config.get().asFile.readText())

        val android = config.android.fromFlavor(flavor.get())
        val ios = config.ios.fromFlavor(flavor.get())
        val common = (android + ios).distinctBy { it.name }.toSet()

        require(android.size == common.size) { "Android build config is missing some values: ${common - android}" }
        require(ios.size == common.size) { "iOS build config is missing some values: ${common - ios}" }

        common.generateExpect()
        android.generateActual(OutputDir.android)
        ios.generateActual(OutputDir.ios)
    }

    private fun Set<BuildConfig>.generateExpect() {
        generateBuildConfig(KModifier.EXPECT, OutputDir.common)
    }

    private fun Set<BuildConfig>.generateActual(outputDir: OutputDir) {
        generateBuildConfig(KModifier.ACTUAL, outputDir)
    }

    private fun Set<BuildConfig>.generateBuildConfig(modifier: KModifier, outputDir: OutputDir) {
        val properties = map { config ->
            PropertySpec.builder(
                config.name,
                ClassName("kotlin", config.type),
            ).apply {
                if (modifier == KModifier.ACTUAL) {
                    addModifiers(KModifier.ACTUAL, KModifier.CONST)
                        .initializer("%L", config.value)
                }
            }.build()
        }

        val buildConfigType = TypeSpec.objectBuilder(FILENAME)
            .addModifiers(modifier)
            .addProperties(properties)
            .build()

        val file = FileSpec.builder(packageName.get(), "$FILENAME${outputDir.suffix}")
            .addType(buildConfigType)
            .build()

        file.writeTo(outputDirs.getting(outputDir).get().asFile)
    }
}

@Serializable
private data class Config(
    @SerialName("android") val android: Environment,
    @SerialName("ios") val ios: Environment,
) {
    @Serializable
    data class Environment(
        @SerialName("dev") val dev: Set<BuildConfig>,
        @SerialName("beta") val beta: Set<BuildConfig>,
        @SerialName("release") val release: Set<BuildConfig>
    ) {
        @Serializable
        data class BuildConfig(
            @SerialName("type") val type: String,
            @SerialName("name") val name: String,
            @SerialName("value") val value: String
        )

        fun fromFlavor(flavor: String) = when (flavor) {
            "dev" -> dev
            "beta" -> beta
            "release" -> release
            else -> dev
        }
    }
}

@Suppress("EnumEntryName", "EnumNaming")
internal enum class OutputDir(
    internal val suffix: String,
    internal val sourceSet: String,
) {
    common("", "commonMain"),
    android(".android", "androidMain"),
    ios(".ios", "iosMain"),
}

private const val FILENAME = "KatanaBuildConfig"
private const val GENERATED_DIR = "generated/source/buildConfig"
