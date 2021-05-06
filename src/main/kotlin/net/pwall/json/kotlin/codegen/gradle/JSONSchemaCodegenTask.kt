/*
 * @(#) JSONSchemaCodegenTask.kt
 *
 * json-kotlin-gradle  Gradle Code Generation Plugin for JSON Schema
 * Copyright (c) 2021 Peter Wall
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package net.pwall.json.kotlin.codegen.gradle

import java.io.File

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import org.gradle.kotlin.dsl.the

import net.pwall.json.JSON
import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.codegen.CodeGenerator
import net.pwall.json.schema.codegen.TargetLanguage
import net.pwall.json.schema.parser.Parser
import net.pwall.yaml.YAMLSimple

@Suppress("UnstableApiUsage")
open class JSONSchemaCodegenTask : DefaultTask() {

    init {
        println("Initialising CodegenTask")
    }

    @TaskAction
    fun generate() {
        val ext: JSONSchemaCodegen = project.the()
        ext.inputFile.orNull?.let { inputFile ->
            val schema = when (inputFile.name.substringAfterLast('.')) {
                "yaml", "yml" -> YAMLSimple.process(inputFile).rootNode
                "json" -> JSON.parse(inputFile)
                else -> throw IllegalArgumentException("Can't process schema file - $inputFile")
            } ?: throw IllegalArgumentException("Schema file is null - $inputFile")
            val language = ext.language.get()
            val outputDir = ext.outputDir.orNull ?: File("build/generated-sources/$language")
            CodeGenerator().apply {
                schemaParser = Parser().apply {
                    customValidationHandler = { key, uri, pointer, value ->
                        ext.schemaExtensions.find {
                            it.keyword.get() == key && it.value.get() == value.toString()
                        }?.validator(uri, pointer)
                    }
                }
                targetLanguage = when (language) {
                    "kotlin" -> TargetLanguage.KOTLIN
                    "java" -> TargetLanguage.JAVA
                    "typescript" -> TargetLanguage.TYPESCRIPT
                    else -> throw IllegalArgumentException("Unrecognised language - $language")
                }
                nestedClassNameOption = CodeGenerator.NestedClassNameOption.USE_NAME_FROM_PROPERTY
                basePackageName = ext.packageName.get()
                baseDirectoryName = outputDir.path
                ext.classMappings.forEach {
                    it.applyTo(this)
                }
                ext.generatorComment.orNull?.let { generatorComment = it }
                generateAll(schema, JSONPointer(ext.pointer.get()))
            }
            println("Generation complete")
        }
    }

}
