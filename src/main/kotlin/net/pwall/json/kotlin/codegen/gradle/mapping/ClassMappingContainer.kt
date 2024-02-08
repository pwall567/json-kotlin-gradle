/*
 * @(#) ClassMappingContainer.kt
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

import kotlin.reflect.KClass
import java.net.URI
import groovy.lang.Closure

import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer

interface ClassMappingContainer : ExtensiblePolymorphicDomainObjectContainer<ClassMapping> {

    fun byFormat(): ClassMappingByFormat

    fun byFormat(closure: Closure<*>): ClassMappingByFormat

    fun byFormat(action: Action<in ClassMappingByFormat>): ClassMappingByFormat

    @Suppress("unused")
    fun byFormat(className: String, keyword: String) = byFormat {
        this.className.set(className)
        this.keyword.set(keyword)
    }

    @Suppress("unused")
    fun byFormat(classRef: KClass<*>, keyword: String) = byFormat {
        this.className.set(classRef.qualifiedName)
        this.keyword.set(keyword)
    }

    fun byExtension(): ClassMappingByExtension

    fun byExtension(closure: Closure<*>): ClassMappingByExtension

    fun byExtension(action: Action<in ClassMappingByExtension>): ClassMappingByExtension

    @Suppress("unused")
    fun byExtension(className: String, keyword: String, value: String) = byExtension {
        this.className.set(className)
        this.keyword.set(keyword)
        this.value.set(value)
    }

    @Suppress("unused")
    fun byExtension(classRef: KClass<*>, keyword: String, value: String) = byExtension {
        this.className.set(classRef.qualifiedName)
        this.keyword.set(keyword)
        this.value.set(value)
    }

    fun byURI(): ClassMappingByURI

    fun byURI(closure: Closure<*>): ClassMappingByURI

    fun byURI(action: Action<in ClassMappingByURI>): ClassMappingByURI

    @Suppress("unused")
    fun byURI(className: String, uri: URI) = byURI {
        this.className.set(className)
        this.uri.set(uri)
    }

    @Suppress("unused")
    fun byURI(classRef: KClass<*>, uri: URI) = byURI {
        this.className.set(classRef.qualifiedName)
        this.uri.set(uri)
    }

}
