/*
 * @(#) InputsContainer.kt
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
import java.net.URI

import groovy.lang.Closure

import org.gradle.api.Action
import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer

interface InputsContainer : ExtensiblePolymorphicDomainObjectContainer<InputDefinition> {

    fun inputFile(): InputFile

    fun inputFile(closure: Closure<*>): InputFile

    fun inputFile(action: Action<in InputFile>): InputFile

    @Suppress("unused")
    fun inputFile(file: File) = inputFile {
        this.file.set(file)
    }

    @Suppress("unused")
    fun inputFile(file: File, subPackage: String) = inputFile {
        this.file.set(file)
        this.subPackage.set(subPackage)
    }

    fun inputComposite(): InputComposite

    fun inputComposite(closure: Closure<*>): InputComposite

    fun inputComposite(action: Action<in InputComposite>): InputComposite

    @Suppress("unused")
    fun inputComposite(file: File, pointer: String) = inputComposite {
        this.file.set(file)
        this.pointer.set(pointer)
    }

    fun inputURI(): InputURI

    fun inputURI(closure: Closure<*>): InputURI

    fun inputURI(action: Action<in InputURI>): InputURI

    @Suppress("unused")
    fun inputURI(uri: URI) = inputURI {
        this.uri.set(uri)
    }

    fun inputCompositeURI(): InputCompositeURI

    fun inputCompositeURI(closure: Closure<*>): InputCompositeURI

    fun inputCompositeURI(action: Action<in InputCompositeURI>): InputCompositeURI

    @Suppress("unused")
    fun inputCompositeURI(uri: URI, pointer: String) = inputCompositeURI {
        this.uri.set(uri)
        this.pointer.set(pointer)
    }

}
