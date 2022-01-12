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

import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.codegen.CodeGenerator
import net.pwall.json.schema.codegen.TargetLanguage
import net.pwall.json.schema.parser.Parser

@Suppress("UnstableApiUsage")
open class JSONSchemaCodegenTask : DefaultTask() {

    @TaskAction
    fun generate() {
        val ext: JSONSchemaCodegen = project.the()
        CodeGenerator().apply {
            val parser = schemaParser ?: Parser().also { schemaParser = it }
            nestedClassNameOption = CodeGenerator.NestedClassNameOption.USE_NAME_FROM_PROPERTY
            val configFile = ext.configFile.orNull ?:
                    File("src/main/resources/codegen-config.json").takeIf { it.exists() }
            configFile?.let { configure(it) }
            if (ext.schemaExtensions.isNotEmpty()) {
                schemaParser = parser.apply {
                    customValidationHandler = { key, uri, pointer, value ->
                        ext.schemaExtensions.find {
                            it.keyword.get() == key && it.value.get() == value.toString()
                        }?.validator(uri, pointer)
                    }
                }
            }
            ext.packageName.orNull?.let { basePackageName = it }
            ext.language.orNull?.let {
                targetLanguage = when (it) {
                    "kotlin" -> TargetLanguage.KOTLIN
                    "java" -> TargetLanguage.JAVA
                    "typescript" -> TargetLanguage.TYPESCRIPT
                    else -> throw IllegalArgumentException("Unrecognised language - $it")
                }
            }
            val outputDir = ext.outputDir.orNull ?: File("build/generated-sources/${targetLanguage.directory()}")
            baseDirectoryName = outputDir.path
            ext.classMappings.forEach {
                it.applyTo(this)
            }
            ext.generatorComment.orNull?.let { generatorComment = it }
            val inputFile = ext.inputFile.orNull ?: File("src/main/resources/schema")
            val include = ext.include.orNull ?: emptyList()
            val exclude = ext.exclude.orNull ?: emptyList()
            ext.pointer.orNull?.let {
                generateAll(parser.jsonReader.readJSON(inputFile), JSONPointer(it)) { name ->
                    (include.isEmpty() || name in include) && (exclude.isEmpty() || name !in exclude)
                }
            } ?: generate(inputFile)
        }
        println("Generation complete")
    }

    private fun TargetLanguage.directory() = when (this) {
        TargetLanguage.KOTLIN -> "kotlin"
        TargetLanguage.JAVA,
        TargetLanguage.JAVA16 -> "java"
        TargetLanguage.TYPESCRIPT -> "ts"
        TargetLanguage.MARKDOWN -> "doc"
    }

}
