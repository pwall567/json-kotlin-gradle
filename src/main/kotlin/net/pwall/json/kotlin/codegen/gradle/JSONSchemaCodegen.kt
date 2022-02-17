/*
 * @(#) JSONSchemaCodegen.kt
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

import org.gradle.api.Action
import org.gradle.api.Project
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

import net.pwall.json.kotlin.codegen.gradle.extension.SchemaExtension
import net.pwall.json.kotlin.codegen.gradle.extension.SchemaExtensionContainer
import net.pwall.json.kotlin.codegen.gradle.extension.SchemaExtensionContainerImpl
import net.pwall.json.kotlin.codegen.gradle.input.InputDefinition
import net.pwall.json.kotlin.codegen.gradle.input.InputsContainer
import net.pwall.json.kotlin.codegen.gradle.input.InputsContainerImpl
import net.pwall.json.kotlin.codegen.gradle.mapping.ClassMapping
import net.pwall.json.kotlin.codegen.gradle.mapping.ClassMappingContainer
import net.pwall.json.kotlin.codegen.gradle.mapping.ClassMappingContainerImpl

open class JSONSchemaCodegen(project: Project) {

    val configFile = project.objects.property<File>()

    val packageName = project.objects.property<String>()

    val inputFile = project.objects.property<File>()

    val language = project.objects.property<String>()

    val pointer = project.objects.property<String>()

    val include = project.objects.listProperty<String>()

    val exclude = project.objects.listProperty<String>()

    val outputDir = project.objects.property<File>()

    val generatorComment = project.objects.property<String>()

    val classMappings: ClassMappingContainer = ClassMappingContainerImpl(project,
            project.objects.polymorphicDomainObjectContainer(ClassMapping::class.java))

    val schemaExtensions: SchemaExtensionContainer = SchemaExtensionContainerImpl(project,
            project.objects.polymorphicDomainObjectContainer(SchemaExtension::class.java))

    val inputs: InputsContainer = InputsContainerImpl(project,
            project.objects.polymorphicDomainObjectContainer(InputDefinition::class.java))

    @Suppress("unused")
    fun classMappings(action: Action<in ClassMappingContainer>) {
        action.execute(classMappings)
    }

    @Suppress("unused")
    fun schemaExtensions(action: Action<in SchemaExtensionContainer>) {
        action.execute(schemaExtensions)
    }

    @Suppress("unused")
    fun inputs(action: Action<in InputsContainer>) {
        action.execute(inputs)
    }

    companion object {
        internal const val NAME = "jsonSchemaCodegen"
    }

}
