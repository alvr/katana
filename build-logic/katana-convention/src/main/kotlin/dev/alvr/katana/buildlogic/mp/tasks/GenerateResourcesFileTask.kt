package dev.alvr.katana.buildlogic.mp.tasks

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import dev.alvr.katana.buildlogic.ResourcesDir
import dev.alvr.katana.buildlogic.mp.identifier
import java.io.File
import org.gradle.api.DefaultTask
import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.CacheableTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.PathSensitive
import org.gradle.api.tasks.PathSensitivity
import org.gradle.api.tasks.TaskAction
import org.gradle.internal.FileUtils

@CacheableTask
internal abstract class GenerateResourcesFileTask : DefaultTask() {
    @get:Input
    internal abstract val packageName: Property<String>

    @get:InputFiles
    @get:PathSensitive(value = PathSensitivity.RELATIVE)
    internal abstract val inputFiles: ConfigurableFileCollection

    @get:OutputDirectory
    internal abstract val outputDir: DirectoryProperty

    private val String.resourceIdentifier
        get() = split('_').identifier

    @TaskAction
    private fun generateResourcesFile() {
        inputFiles.files.generateResourcesFile()
    }

    private fun Set<File>.generateResourcesFile() {
        if (isEmpty()) {
            outputDir.get().asFile.deleteRecursively()
            return
        }

        val stableAnnotation = AnnotationSpec
            .builder(ClassName(ComposeRuntimePackage, ComposeStableAnnotation))
            .build()

        val properties = map { file ->
            val propertyName = FileUtils.removeExtension(file.name).resourceIdentifier
            val fileName = file.absolutePath.replace('\\', '/').substringAfter("$ResourcesDir/")

            PropertySpec.builder(propertyName, ClassName(KatanaResourcePackage, KatanaResourceClass))
                .addAnnotation(stableAnnotation)
                .addModifiers(KModifier.INTERNAL)
                .initializer("%L(%S)", KatanaResourceClass, fileName)
                .build()
        }

        val resourcesObject = TypeSpec.objectBuilder(KatanaResourcesLocalClass)
            .addModifiers(KModifier.INTERNAL)
            .addProperties(properties)
            .build()

        FileSpec.builder(ClassName("${packageName.get()}$KatanaResourcesLocalPackage", KatanaResourcesLocalClass))
            .addType(resourcesObject)
            .build()
            .writeTo(outputDir.get().asFile)
    }

    private companion object {
        const val ComposeRuntimePackage = "androidx.compose.runtime"
        const val ComposeStableAnnotation = "Stable"

        const val KatanaResourcePackage = "dev.alvr.katana.ui.base.resources"
        const val KatanaResourceClass = "KatanaResource"

        const val KatanaResourcesLocalPackage = ".resources"
        const val KatanaResourcesLocalClass = "KatanaResources"
    }
}
