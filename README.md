# json-kotlin-gradle

[![Build Status](https://github.com/pwall567/json-kotlin-gradle/actions/workflows/build.yml/badge.svg)](https://github.com/pwall567/json-kotlin-gradle/actions/workflows/build.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/static/v1?label=Kotlin&message=v2.0.21&color=7f52ff&logo=kotlin&logoColor=7f52ff)](https://github.com/JetBrains/kotlin/releases/tag/v2.0.21)
[![Maven Central](https://img.shields.io/maven-central/v/net.pwall.json/json-kotlin-gradle?label=Maven%20Central)](https://central.sonatype.com/artifact/net.pwall.json/json-kotlin-gradle)

Gradle JSON Schema code generation plugin.

## NEW

The means of specifying input files to the generation process has been extended; see the [`inputs`](#inputs) section.

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
        classpath("net.pwall.json:json-kotlin-gradle:0.121")
    }
}

apply<JSONSchemaCodegenPlugin>()
```
The `apply` line should come after the `buildscript` block, and **before** any `configure` block (see below).

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
    packageName.set("your.package.name") // if not specified in a config file
    generatorComment.set("comment...")
    inputs {
        inputFile(file("path/to/your/schema/file/or/files")) // if not in the default location
        inputFile {
            file.set(file("path/to/your/schema/file/or/files")) // alternative syntax for above
        }
        inputComposite {
            file.set(file("path/to/your/schema/composite/file"))
            pointer.set("/\$defs") // a JSON Pointer to the group of definitions within the file
        }
        inputURI(uri("https://example.com/path/to/file"))
        inputCompositeURI(uri("https://example.com/path/to/composite"), "\$defs")
        // inputFile, inputComposite, inputURI and inputCompositeURI
        // may be repeated as necessary in any combination
    }
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

### `inputs`

The `inputs` section specifies the location(s) of the input files.
By default, the plugin will process all files in the `src/main/resources/schema` directory (and subdirectories);
to specify a different location or a combination of inputs, the `inputs` section allows multiple `inputFile` or
`inputComposite` definitions.

### `inputFile`

This specifies an individual file or directory of files to be added to the list of files to be processed.
```kotlin
    inputs {
        inputFile {
            file.set(file("path/to/your/schema/file/or/files"))
            subPackage.set("sub.package.name") // optional sub-package name
        }
        inputFile(file("path/to/your/schema/file/or/files")) // alternative syntax
        inputFile(file("path/to/your/schema/file/or/files"), "sub.package.name") // alternative syntax
    }
```
If a `subPackage` name is supplied, the class or classes generated from this `inputFile` declaration will be output to
the package _base.subPackage_ where _base_ is the base package from the [`packageName`](#packagename) declaration or the
config file, and _subPackage_ is the name given here.
The name may be a single name or a structured name using dot separators.

As many `inputFile` entries as are required may be specified, and they may be combined with other forms of input
specification.

### `inputComposite`

One of the common uses of the code generator is to generate a set of classes from a composite file.
For example, an OpenAPI file may contain a number of schema definitions for the request and response objects of the API,
or a JSON Schema file may contain a set of definitions in the `$defs` section.

To specify this type of usage:
```kotlin
    inputs {
        inputComposite {
            file.set(file("path/to/your/composite/file"))
            pointer.set("/\$defs")
            include.set(listof("IncludeThis", "AndThis")) // optional - specifies classes to include
            exclude.set(listof("NotThis", "NorThis")) // optional - sepcifies files to exclude
        }
        inputComposite(file.set(file("path/to/your/composite/file")), "\$defs") // alternative syntax
    }
```
As many `inputComposite` entries as are required may be specified, and they may be combined with other forms of input
specification.

### `inputURI`

Many organisations will make their schema files available via a public URL.
Others will have a local schema repository accessible within their own VPN.
The code generator can generate files directly from a URI:
```kotlin
    inputs {
        inputURI {
            uri.set(uri("https://local.domain.com/schema/customer.json"))
        }
        inputURI(uri("https://local.domain.com/schema/customer.json")) // alternative syntax
    }
```
In this case, the URI must be a valid URL pointing to a downloadable file.

As many `inputURI` entries as are required may be specified, and they may be combined with other forms of input
specification.

### `inputCompositeURI`

Composite files may also be accessed by URI:
```kotlin
    inputs {
        inputCompositeURI {
            uri.set(uri("https://local.domain.com/schema/api.json"))
            pointer.set("/\$defs")
            include.set(listof("IncludeThis", "AndThis")) // optional - specifies classes to include
            exclude.set(listof("NotThis", "NorThis")) // optional - sepcifies files to exclude
        }
        inputCompositeURI(uri("https://local.domain.com/schema/api.json"), "\$defs") // alternative syntax
    }
```
The URI must be a valid URL pointing to a downloadable file, and the pointer string must select a sub-tree within that
file.
To include two separate sections of the same file, include two separate `inputCompositeURI` entries with the same URI
but different pointers (and possibly include/exclude settings).

As many `inputCompositeURI` entries as are required may be specified, and they may be combined with other forms of input
specification.

### `include` and `exclude`

To include only a nominated subset of definitions from a combined file (using [`inputComposite`](#inputcomposite) or
[`inputCompositeURI`](#inputcompositeuri)), specify the names of the definitions to be included (optional &ndash; see
example above).

Alternatively, to exclude nominated definitions from a combined file, specify the names of the definitions to be
excluded.

If both `include` and `exclude` are supplied for the same composite, both will be applied, but this clearly
doesn&rsquo;t make a great deal of sense since in order to be excluded, a definition must first have been explicitly
included.

The alternative (shorter) syntax for `inputComposite` or `inputCompositeURI` does not provide for the specification of
`include` or `exclude`.

### `configFile`

If the config file is not in the default location of `src/main/resources/codegen-config.json`, the location of the file
may be specified using the `configFile` property:
```kotlin
    configFile.set(file("path/to/your/config.json"))
```

### `inputFile`

This is the earlier means of specifying the input file or files;
the [`inputs`](#inputs) section above is more flexible, and the older form may be deprecated in future.
```kotlin
    inputFile.set(file("path/to/your/schema/file/or/files"))
```

### `pointer`

This is the earlier means of specifying a composite input file;
the [`inputs`](#inputs) section above is more flexible, and the older form may be deprecated in future.
```kotlin
    inputFile.set(file("path/to/your/file")) // must be a single file, and the default is not allowed
    pointer.set("/pointer/to/definitions") // JSON Pointer to the object containing the definitions
```

### `include`

This is the earlier means of specifying classes to be included from a composite input file;
the [`inputs`](#inputs) section above is more flexible, and the older form may be deprecated in future.
```kotlin
    include.set(listof("IncludeThis", "AndThis"))
```

### `exclude`

This is the earlier means of specifying classes to be included from a composite input file;
the [`inputs`](#inputs) section above is more flexible, and the older form may be deprecated in future.
```kotlin
    exclude.set(listof("NotThis", "NorThis"))
```

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
        classpath("net.pwall.json:json-kotlin-gradle:0.121")
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
(The `classMappings` and `schemaExtensions` sections are not available in Groovy.
The `inputs` section is also not available &ndash; the `inputFile` and `pointer` definitions as shown in the above
example must be used instead.)

## Gradle Warnings

This plugin has been tested with Gradle version 8.1.1.
The build process gives warnings about features that will be incompatible with future Gradle versions, but for the
moment it seems to be OK.

(For Maven users, there is a Maven equivalent to this plugin &ndash;
[`json-kotlin-maven`](https://github.com/pwall567/json-kotlin-maven).)

Peter Wall

2025-04-13
