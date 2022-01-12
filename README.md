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
The `json-kotlin-gradle` plugin simplifies the use of this code generation mechanism from the
[Gradle Build Tool](https://gradle.org/).

## To Use

Include the following at the start of your `build.gradle.kts` file:
```kotlin
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenPlugin
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegen // only required if "configure" block included

buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("net.pwall.json:json-kotlin-gradle:0.67.1")
    }
}

apply<JSONSchemaCodegenPlugin>()
```

And to have the generated source files compiled:
```kotlin
sourceSets.main {
    java.srcDirs("build/generated-sources/kotlin")
}
```
This shows the use of the default output directory; if it is changed by the use of the [`outputDir`](#outputdir)
configuration property, this setting will also change.
The `srcDirs` function takes a `vararg` list; if other source files are to be included in the compilation task the
directories may be added to the function call.

The plugin follows the principle of &ldquo;convention over configuration&rdquo;.
The default location for the schema file or files to be input to the generation process is:
```
    «project root»/src/main/resources/schema
```
and the default location for the
[config file](https://github.com/pwall567/json-kotlin-schema-codegen/blob/main/CONFIG.md) (if required) is:
```
    «project root»/src/main/resources/codegen-config.json
```
If your files are in these default locations, the above additions to `build.gradle.kts` will be all you will need.

If you wish to specify the location of your schema or config files, or if you wish to use any of the other plugin
customisation options, you may include a configuration block as follows:
```kotlin
configure<JSONSchemaCodegen> {
    configFile.set(file("path/to/your/config.json")) // if not in the default location
    inputFile.set(file("path/to/your/schema/file/or/files")) // if not in the default location
    packageName.set("your.package.name") // if not specified in a config file
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

If any setting in this configuration block conflicts with the equivalent setting in the config file, this configuration
block (in `build.gradle.kts`) takes precedence.

## Configuration Options

### `configFile`

If the config file is not in the default location of `src/main/resources/codegen-config.json`, the location of the file
may be specified using the `configFile` property:
```kotlin
    configFile.set(file("path/to/your/config.json"))
```

### `inputFile`

The input to the code generation process may be a single file or a directory tree containing a number of schema files.
By default, the plugin will process all files in the `src/main/resources/schema` directory (and subdirectories), but to
specify an alternative file or directory, use:
```kotlin
    inputFile.set(file("path/to/your/schema/file/or/files"))
```

### `pointer`

One of the common uses of the code generator is to generate a set of classes from a composite file.
For example, an OpenAPI file may contain a number of schema definitions for the request and response objects of the API,
or a JSON Schema file may contain a set of definitions in the `$defs` section.

To specify this form of usage to the plugin, the configuration block must contain:
```kotlin
    inputFile.set(file("path/to/your/file")) // must be a single file, and the default is not allowed
    pointer.set("/pointer/to/definitions") // JSON Pointer to the object containing the definitions
```

For example, to process the entire set of schema definitions in an OpenAPI file:
```kotlin
    inputFile.set(file("src/main/resources/openapi/openapi.yaml"))
    pointer.set("/components/schemas")
```

### `include`

To include only a nominated subset of definitions from a combined file (using [`pointer`](#pointer)), specify the names
of the definitions to be included:
```kotlin
    include.set(listof("IncludeThis", "AndThis"))
```

### `exclude`

Alternatively, to exclude nominated definitions from a combined file, specify the names of the definitions to be
excluded:
```kotlin
    exclude.set(listof("NotThis", "NorThis"))
```

If both `include` and `exclude` are supplied in the same build, both will be applied, but this clearly doesn&rsquo;t
make a great deal of sense since in order to be excluded, a definition must first have been explicitly included.

### `outputDir`

The default output directory is `build/generated-sources/«language»`, where `«language»` is `kotlin`, `java` or
`ts` (for TypeScript), depending on the target language.
To specify a different output directory, use:
```kotlin
    outputDir.set(file("path/to/your/output/directory"))
```

### `language`

The default target language is Kotlin (naturally enough, for a project written in Kotlin and primarily intended to
target Kotlin).
To specify Java output:
```kotlin
    language.set(file("java"))
```
The value of this setting must be `kotlin`, `java` or `typescript`.

This setting may be specified in the config file, and that is the recommended practice.

### `packageName`

By default, classes will be generated without a `package` directive.
To supply a package name:
```kotlin
    packageName.set(file("com.example.model"))
```

This setting may be specified in the config file, and that is the recommended practice.

### `generatorComment`

The `generatorComment` allows the provision of a line of text to be added to the comment block output to the start of
each generated file:
```kotlin
    generatorComment.set("This was generated from Schema version 1.0")
```

This setting may be specified in the config file, and that is the recommended practice.

### `classMappings`

This property allows the specification of custom types to be used for class properties or array items.
It has been superseded by the
[`customClasses`](https://github.com/pwall567/json-kotlin-schema-codegen/blob/main/CONFIG.md#customclasses) facility of
the config file, and will probably be deprecated in future releases and eventually removed.

### `schemaExtensions`

This property allows the specification of custom schema extensions to add validations to be mapped to certain
keyword-value combinations.
It has been superseded by the
[`extensionValidations`](https://github.com/pwall567/json-kotlin-schema-codegen/blob/main/CONFIG.md#extensionvalidations)
facility of the config file, and will probably be deprecated in future releases and eventually removed.

## Groovy

Many people prefer to use the Groovy syntax for Gradle files, even for Kotlin-based projects.
In this case, the `buildscript` at the start of the `build.gradle` file will be:
```groovy
import net.pwall.json.kotlin.codegen.gradle.JSONSchemaCodegenPlugin

buildscript {
    repositories {
        mavenLocal()
    }
    dependencies {
        classpath("net.pwall.json:json-kotlin-gradle:0.66")
    }
}
```
and the `apply` statement must be added **after** any `plugins` block:
```groovy
apply plugin: JSONSchemaCodegenPlugin
```

To compile the generated sources:
```groovy
sourceSets {
    main.kotlin.srcDirs += "build/generated-sources/kotlin"
}
```

Then, the configuration block (if required) will be something like:
```groovy
jsonSchemaCodegen {
    configFile = file('path/to/your/config.json')
    inputFile = file('path/to/your/file/or/files')
    pointer = '/pointer'
    exclude = ['ExcludeThis', 'AndThis']
    packageName = 'com.example.model'
    generatorComment = 'This was generated from Schema version 1.0'
}
```
(The `classMappings` and `schemaExtensions` sections are not available in Groovy.)

## Observations on Gradle

The examples shown above are correct at the time of writing, but I have found Gradle to be an exceptionally volatile
environment to work with.
Functions that work in one version are frequently marked as deprecated in the next, and removed entirely in the version
following that.

I use Maven for most of my own work, and while that system is far from perfect, it remains stable and functional and
doesn't require an intimate knowledge of the inner workings of the build tool to achieve satisfactory results.

(For Maven users, there is a Maven equivalent to this plugin &ndash;
[`json-kotlin-maven`](https://github.com/pwall567/json-kotlin-maven).)

Peter Wall

2022-01-12
