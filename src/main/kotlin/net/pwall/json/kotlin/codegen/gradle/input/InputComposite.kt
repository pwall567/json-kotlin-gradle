/*
 * @(#) InputComposite.kt
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

import java.io.File
import javax.inject.Inject

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.listProperty
import org.gradle.kotlin.dsl.property

import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenTask.Companion.addPointerTargets
import net.pwall.json.schema.codegen.CodeGenerator

class InputComposite @Inject constructor(name: String, project: Project) : InputDefinition(name, project) {

    @Input
    val file = project.objects.property<File>()

    @Input
    val pointer = project.objects.property<String>()

    @Input
    val include = project.objects.listProperty<String>()

    @Input
    val exclude = project.objects.listProperty<String>()

    override fun applyTo(codeGenerator: CodeGenerator) {
        file.orNull?.let {
            val includes = include.orNull ?: emptyList()
            val excludes = exclude.orNull ?: emptyList()
            val ptr = pointer.orNull ?: throw IllegalArgumentException("No pointer specified")
            codeGenerator.addPointerTargets(it, ptr, includes, excludes)
        } ?: throw IllegalArgumentException("No File specified")
    }

}
