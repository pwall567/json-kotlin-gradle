/*
 * @(#) ClassMapping.kt
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

package net.pwall.json.kotlin.codegen.gradle.mapping

import javax.inject.Inject

import org.gradle.api.Named
import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.property

import net.pwall.json.schema.codegen.ClassId
import net.pwall.json.schema.codegen.ClassName
import net.pwall.json.schema.codegen.CodeGenerator

abstract class ClassMapping @Inject constructor(@Input val name0: String, project: Project) : Named {

    override fun getName(): String = name0

    @Input
    val className = project.objects.property<String>()

    abstract fun applyTo(codeGenerator: CodeGenerator)

    val classId: ClassId
        get() = className.get().let {
            val i = it.lastIndexOf('.')
            if (i < 0)
                ClassName(it)
            else
                ClassName(it.substring(i + 1), it.substring(0, i))
        }

}
