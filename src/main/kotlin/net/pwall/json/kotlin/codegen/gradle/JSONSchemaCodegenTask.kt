/*
 * @(#) JSONSchemaCodegenTask.kt
 *
 * json-kotlin-gradle  Gradle Code Generation Plugin for JSON Schema
 * Copyright (c) 2021, 2022 Peter Wall
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

open class JSONSchemaCodegenTask : DefaultTask() {

    @TaskAction
    fun generate() {
        val ext: JSONSchemaCodegen = project.the()
        CodeGenerator().apply {
            val parser = schemaParser
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
            ext.inputs.forEach {
                it.preload(this)
            }
            ext.inputs.forEach {
                it.applyTo(this)
            }
            val inputFile = ext.inputFile.orNull
            val pointer = ext.pointer.orNull
            val includes = ext.include.orNull ?: emptyList()
            val excludes = ext.exclude.orNull ?: emptyList()
            when {
                inputFile != null -> {
                    if (pointer != null)
                        addPointerTargets(inputFile, pointer, includes, excludes)
                    else
                        addTargets(listOf(inputFile))
                }
                pointer != null -> throw IllegalArgumentException("Pointer with no composite input file")
                numTargets == 0 -> addTargets(listOf(defaultInputLocation))
            }
            generateAllTargets()
        }
        println("Generation complete")
    }

    companion object {

        private val defaultInputLocation = File("src/main/resources/schema")

        private fun TargetLanguage.directory() = when (this) {
            TargetLanguage.KOTLIN -> "kotlin"
            TargetLanguage.JAVA,
            TargetLanguage.JAVA16 -> "java"
            TargetLanguage.TYPESCRIPT -> "ts"
            TargetLanguage.MARKDOWN -> "doc"
        }

        fun CodeGenerator.addPointerTargets(
            file: File,
            pointer: String,
            includes: List<String>,
            excludes: List<String>,
        ) {
            schemaParser.preLoad(file)
            addCompositeTargets(schemaParser.jsonReader.readJSON(file), JSONPointer(pointer)) { name ->
                (includes.isEmpty() || name in includes) && (excludes.isEmpty() || name !in excludes)
            }
        }

    }

}
