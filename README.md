# json-kotlin-gradle

[![Build Status](https://travis-ci.com/pwall567/json-kotlin-gradle.svg?branch=main)](https://travis-ci.com/github/pwall567/json-kotlin-gradle)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v1.5.20&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v1.5.20)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.json/json-kotlin-gradle?label=Maven%20Central)](https://search.maven.org/search?q=g:%22net.pwall.json%22%20AND%20a:%22json-kotlin-gradle%22)

Gradle JSON Schema code generation plugin.

## Background

The [`json-kotlin-schema-codegen`](https://github.com/pwall567/json-kotlin-schema-codegen) project provides a means of
generating Kotlin or Java classes (or TypeScript interfaces) from [JSON Schema](https://json-schema.org/) object
descriptions.
The `json-kotlin-gradle` plugin simplifies the use of this code generation mechanism.

## To Use

Include the following at the start of your `build.gradle.kts` file:
```kotlin
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenPlugin
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegen

buildscript {
    repositories {
        mavenLocal()
        jcenter()
    }
    dependencies {
        classpath("net.pwall.json:json-kotlin-gradle:0.62")
    }
}

apply<JSONSchemaCodegenPlugin>()
```

Then, to configure the plugin, you will need a block as follows:
```kotlin
configure<JSONSchemaCodegen> {
    packageName.set("your.package.name")
    inputFile.set(file("path/to/your/file")) // probably in src/main/resources/...
    pointer.set("/\$defs") // a JSON Pointer to the group of definitions within the file
    generatorComment.set("comment...")
    classMappings { // configure specific class mappings if required
        byFormat("java.time.Duration", "duration")
    }
    schemaExtensions { // configure extension keyword uses if required
        patternValidation("x-type", "account-number", Regex("^[0-9]{4,10}$"))
    }
}
```
