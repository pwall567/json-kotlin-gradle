# Change Log

The format is based on [Keep a Changelog](http://keepachangelog.com/).

The major and minor version numbers of this repository (but not patch numbers) match the version numbers of the
[`json-kotlin-schema-codegen`](https://github.com/pwall567/json-kotlin-schema-codegen) library used by this Gradle
plugin.

## [0.76] - 2022-04-23
### Changed
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`

## [0.75] - 2022-04-21
### Changed
- `InputFile`, `InputsContainer`: allow sub-package to be specified with file/directory
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`

## [0.73] - 2022-03-27
### Changed
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`

## [0.72.1] - 2022-03-25
### Changed
- `JSONSchemaCodegenTask`, "inputs" classes: preload files before schema parsing

## [0.72] - 2022-03-02
### Changed
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`

## [0.71] - 2022-03-01
### Added
- `InputURI`, `InputCompositeURI`: new

## [0.70] - 2022-02-16
### Changed
- `JSONSchemaCodegen`, `JSONSchemaCodegenTask`: added new `inputs` structure
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`
### Added
- `input` package: new `inputs` structure

## [0.68] - 2022-01-20
### Changed
- `build.gradle.kts`: updated dependency on `json-kotlin-schema-codegen`

## [0.67.1] - 2022-01-12
### Changed
- `JSONSchemaCodegen`, `JSONSchemaCodegenTask`: added `include` and `exclude`

## [0.67] - 2022-01-07
### Changed
- `build.gradle.kts`: updated dependencies

## [0.66] - 2021-12-12
### Changed
- `build.gradle.kts`: updated dependencies
- `JSONSchemaCodegen`, `JSONSchemaCodegenTask`: added `configFile`, changed handling of defaults
- `ClassMappingContainerImpl`: replaced deprecated function calls
- `README.md`: expanded documentation

## [0.64] - 2021-11-18
### Changed
- `build.gradle.kts`: updated dependencies

## [0.62] - 2021-11-16
### Changed
- `build.gradle.kts`: updated dependencies

## [0.61] - 2021-11-14
### Changed
- `build.gradle.kts`: updated dependencies

## [0.59] - 2021-11-09
### Changed
- `build.gradle.kts`: updated dependencies

## [0.58] - 2021-11-09
### Changed
- `build.gradle.kts`: updated dependencies

## [0.55] - 2021-11-08
### Changed
- `build.gradle.kts`: updated dependencies

## [0.54] - 2021-11-07
### Changed
- `build.gradle.kts`: updated dependencies

## [0.53] - 2021-11-07
### Changed
- `build.gradle.kts`: updated dependencies

## [0.52] - 2021-11-07
### Changed
- `build.gradle.kts`: updated dependencies

## [0.50] - 2021-11-05
### Changed
- `build.gradle.kts`: updated dependencies

## [0.49.1] - 2021-11-04
### Changed
- `build.gradle.kts`, `README.md`: fixed error in updating dependencies

## [0.49] - 2021-11-04
### Changed
- `build.gradle.kts`: updated dependencies

## [0.48] - 2021-10-07
### Changed
- `build.gradle.kts`, `README.md`: updated dependencies

## [0.35] - 2021-08-17
### Changed
- `build.gradle.kts`, `README.md`: updated dependencies

## [0.34] - 2021-06-20
### Changed
- `build.gradle.kts`, `README.md`: updated dependencies

## [0.31.4] - 2021-05-11
### Changed
- `ClassMappingContainer`, `SchemaExtensionContainer`, `SchemaExtensionContainerImpl`: simplified configuration
- `build.gradle.kts`, `README.md`: bumped version number, extended example
### Added
- `SchemaExtensionName`: allows grouping of extensions with same name

## [0.31.3] - 2021-05-07
### Changed
- `JSONSchemaCodegen`, `JSONSchemaCodegenTask`: Added `generatorComment`
- `JSONSchemaCodegen`, `JSONSchemaCodegenTask`: Added schema extensions
- `build.gradle.kts`, `README.md`: bumped version number, extended example
### Added
- `SchemaExtension`, `SchemaExtensionContainer`, `SchemaExtensionContainerImpl`, `SchemaExtensionFormatValidation`,
`SchemaExtensionIntValidation`, `SchemaExtensionPatternValidation`: new

### Added
- this file

## [0.31.2] - 2021-05-04
### Added
- all: Initial working version
