/*
 * @(#) SchemaExtensionContainer.kt
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

import groovy.lang.Closure

import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer

interface SchemaExtensionContainer : ExtensiblePolymorphicDomainObjectContainer<SchemaExtension> {

    fun formatValidation(): SchemaExtensionFormatValidation

    fun formatValidation(closure: Closure<*>): SchemaExtensionFormatValidation

    fun formatValidation(action: Action<in SchemaExtensionFormatValidation>): SchemaExtensionFormatValidation

    fun intValidation(): SchemaExtensionIntValidation

    fun intValidation(closure: Closure<*>): SchemaExtensionIntValidation

    fun intValidation(action: Action<in SchemaExtensionIntValidation>): SchemaExtensionIntValidation

    fun patternValidation(): SchemaExtensionPatternValidation

    fun patternValidation(closure: Closure<*>): SchemaExtensionPatternValidation

    fun patternValidation(action: Action<in SchemaExtensionPatternValidation>): SchemaExtensionPatternValidation

}
