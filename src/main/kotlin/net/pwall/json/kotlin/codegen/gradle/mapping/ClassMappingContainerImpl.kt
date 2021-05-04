/*
 * @(#) ClassMappingContainerImpl.kt
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

import groovy.lang.Closure

import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.Project
import org.gradle.util.ConfigureUtil

class ClassMappingContainerImpl(project: Project, delegate: ExtensiblePolymorphicDomainObjectContainer<ClassMapping>) :
        ClassMappingContainer, ExtensiblePolymorphicDomainObjectContainer<ClassMapping> by delegate {

    init {
        registerFactory(ClassMappingByFormat::class.java) { name -> ClassMappingByFormat(name, project) }
        registerFactory(ClassMappingByExtension::class.java) { name -> ClassMappingByExtension(name, project) }
        registerFactory(ClassMappingByURI::class.java) { name -> ClassMappingByURI(name, project) }
    }

    override fun byFormat(): ClassMappingByFormat = byFormat {}

    override fun byFormat(closure: Closure<*>): ClassMappingByFormat {
        return byFormat(ConfigureUtil.configureUsing(closure))
    }

    override fun byFormat(action: Action<in ClassMappingByFormat>): ClassMappingByFormat {
        return create(generatedName, ClassMappingByFormat::class.java) {
            action.execute(this)
        }
    }

    override fun byExtension(): ClassMappingByExtension = byExtension {}

    override fun byExtension(closure: Closure<*>): ClassMappingByExtension {
        return byExtension(ConfigureUtil.configureUsing(closure))
    }

    override fun byExtension(action: Action<in ClassMappingByExtension>): ClassMappingByExtension {
        return create(generatedName, ClassMappingByExtension::class.java) {
            action.execute(this)
        }
    }

    override fun byURI(): ClassMappingByURI = byURI {}

    override fun byURI(closure: Closure<*>): ClassMappingByURI {
        return byURI(ConfigureUtil.configureUsing(closure))
    }

    override fun byURI(action: Action<in ClassMappingByURI>): ClassMappingByURI {
        return create(generatedName, ClassMappingByURI::class.java) {
            action.execute(this)
        }
    }

    companion object {

        private var generationNumber = 0

        val generatedName: String
            get() = "mapping${generationNumber++}"

    }

}
