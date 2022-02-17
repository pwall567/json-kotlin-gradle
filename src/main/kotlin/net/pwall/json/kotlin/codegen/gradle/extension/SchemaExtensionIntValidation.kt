/*
 * @(#) SchemaExtensionIntValidation.kt
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

package net.pwall.json.kotlin.codegen.gradle.extension

import java.net.URI
import javax.inject.Inject

import org.gradle.api.Project
import org.gradle.api.tasks.Input
import org.gradle.kotlin.dsl.property

import net.pwall.json.pointer.JSONPointer
import net.pwall.json.schema.JSONSchema
import net.pwall.json.schema.validation.NumberValidator

class SchemaExtensionIntValidation @Inject constructor(name: String, project: Project) :
        SchemaExtension(name, project) {

    @Input
    val type = project.objects.property<String>()

    @Input
    val number = project.objects.property<Int>()

    override fun validator(uri: URI?, pointer: JSONPointer): JSONSchema.Validator {
        return NumberValidator(uri, pointer, number.get(), when (type.get()) {
            "minimum" -> NumberValidator.ValidationType.MINIMUM
            "maximum" -> NumberValidator.ValidationType.MAXIMUM
            "exclusive-minimum" -> NumberValidator.ValidationType.EXCLUSIVE_MINIMUM
            "exclusive-maximum" -> NumberValidator.ValidationType.EXCLUSIVE_MAXIMUM
            else -> throw IllegalArgumentException("Invalid comparison type")
        })
    }

}
