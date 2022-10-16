/*
 * @(#) SchemaExtensionContainerImpl.kt
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
import org.gradle.api.Project

class SchemaExtensionContainerImpl(
    val project: Project,
    delegate: ExtensiblePolymorphicDomainObjectContainer<SchemaExtension>,
) : SchemaExtensionContainer, ExtensiblePolymorphicDomainObjectContainer<SchemaExtension> by delegate {

    init {
        registerFactory(SchemaExtensionFormatValidation::class.java) { name ->
            SchemaExtensionFormatValidation(name, project)
        }
        registerFactory(SchemaExtensionIntValidation::class.java) { name ->
            SchemaExtensionIntValidation(name, project)
        }
        registerFactory(SchemaExtensionPatternValidation::class.java) { name ->
            SchemaExtensionPatternValidation(name, project)
        }
    }

    override fun formatValidation(): SchemaExtensionFormatValidation = formatValidation {}

    override fun formatValidation(closure: Closure<*>): SchemaExtensionFormatValidation =
        formatValidation { configure(closure) }

    override fun formatValidation(action: Action<in SchemaExtensionFormatValidation>): SchemaExtensionFormatValidation =
        create(generatedName, SchemaExtensionFormatValidation::class.java) {
            action.execute(this)
        }

    override fun intValidation(): SchemaExtensionIntValidation = intValidation {}

    override fun intValidation(closure: Closure<*>): SchemaExtensionIntValidation = intValidation { configure(closure) }

    override fun intValidation(action: Action<in SchemaExtensionIntValidation>): SchemaExtensionIntValidation =
        create(generatedName, SchemaExtensionIntValidation::class.java) {
            action.execute(this)
        }

    override fun patternValidation(): SchemaExtensionPatternValidation = patternValidation {}

    override fun patternValidation(closure: Closure<*>): SchemaExtensionPatternValidation =
        patternValidation { configure(closure) }

    override fun patternValidation(action: Action<in SchemaExtensionPatternValidation>):
            SchemaExtensionPatternValidation =
        create(generatedName, SchemaExtensionPatternValidation::class.java) {
            action.execute(this)
        }

    override fun extensionName(keyword: String, configure: SchemaExtensionName.() -> Unit) {
        SchemaExtensionName(this, keyword).configure()
    }

    companion object {

        private var generationNumber = 0

        val generatedName: String
            get() = "extension${generationNumber++}"

    }

}
