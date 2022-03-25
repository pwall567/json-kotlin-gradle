/*
 * @(#) InputCompositeURI.kt
 *
 * json-kotlin-gradle  Gradle Code Generation Plugin for JSON Schema
 * Copyright (c) 2022 Peter Wall
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

package net.pwall.json.kotlin.codegen.gradle.input

import java.net.URI
import javax.inject.Inject

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.codegen.CodeGenerator

class InputCompositeURI @Inject constructor(name: String, project: Project) : InputDefinition(name, project) {

    @Input
    val uri = project.objects.property<URI>()

    @Input
    val pointer = project.objects.property<String>()

    @Input
    val include = project.objects.listProperty<String>()

    @Input
    val exclude = project.objects.listProperty<String>()

    override fun preload(codeGenerator: CodeGenerator) {
        codeGenerator.schemaParser.jsonReader.readJSON(checkURI())
    }

    override fun applyTo(codeGenerator: CodeGenerator) {
        val includes = include.orNull ?: emptyList()
        val excludes = exclude.orNull ?: emptyList()
        val ptr = pointer.orNull ?: throw IllegalArgumentException("No pointer specified")
        codeGenerator.addCompositeTargets(checkURI(), JSONPointer(ptr)) { name ->
            (includes.isEmpty() || name in includes) && (excludes.isEmpty() || name !in excludes)
        }
    }

    @Suppress("unused")
    fun include(vararg names: String) {
        include.set(listOf(*names))
    }

    @Suppress("unused")
    fun exclude(vararg names: String) {
        exclude.set(listOf(*names))
    }

    private fun checkURI() = uri.orNull ?: throw IllegalArgumentException("No URI specified")

}
