package net.pwall.json.kotlin.codegen.gradle

import java.io.File
import net.pwall.json.JSON
import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.codegen.CodeGenerator
import net.pwall.json.schema.codegen.TargetLanguage
import net.pwall.yaml.YAMLSimple
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputDirectory
import org.gradle.api.tasks.TaskAction

open class CodegenTask : DefaultTask() {

    @Input
    var language = "kotlin"

    @InputFile
    var inputFile: File? = null

    @Input
    val pointer: String? = null

    @OutputDirectory
    var outputDir = File("build/generated-sources")

    @Input
    var packageName: String? = null

    @TaskAction
    fun generate() {
        val file = inputFile ?: throw IllegalArgumentException("inputFile not specified")
        val schema = when (file.name.substringAfterLast('.')) {
            "yaml", "yml" -> YAMLSimple.process(file).rootNode
            "json" -> JSON.parse(file)
            else -> throw IllegalArgumentException("Can't process schema file - $file")
        } ?: throw IllegalArgumentException("Schema file is null - $file")
        val basePointer: JSONPointer = pointer?.let { JSONPointer(it) } ?: JSONPointer.root
        val codeGenerator = CodeGenerator()
        codeGenerator.targetLanguage = when (language) {
            "kotlin" -> TargetLanguage.KOTLIN
            "java" -> TargetLanguage.JAVA
            "typescript" -> TargetLanguage.TYPESCRIPT
            else -> throw IllegalArgumentException("Unrecognised language - $language")
        }
        codeGenerator.nestedClassNameOption = CodeGenerator.NestedClassNameOption.USE_NAME_FROM_PROPERTY
        codeGenerator.basePackageName = packageName
        codeGenerator.baseDirectoryName = File(outputDir, language).path
        codeGenerator.generateAll(schema, basePointer)
    }

}
