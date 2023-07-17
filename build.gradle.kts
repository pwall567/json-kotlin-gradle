/*
 * @(#) build.gradle.kts
 */

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.pwall.json"
version = "0.91"
description = "Gradle Code Generation Plugin for JSON Schema"

val displayName = "JSON Schema Code Generation Plugin"
val projectURL = "https://github.com/pwall567/${project.name}"

plugins {
    kotlin("jvm") version "1.7.21"
    id("org.jetbrains.dokka") version "1.7.20"
    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
    `kotlin-dsl`
    `maven-publish`
    signing
}

gradlePlugin {
    plugins {
        create("json-kotlin") {
            id = "${group}.json-kotlin"
            implementationClass = "${group}.kotlin.codegen.gradle.JSONSchemaCodegenPlugin"
        }
    }
}

repositories {
    mavenCentral()
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            languageVersion = "1.7"
            jvmTarget = "1.8"
        }
    }
    val sourcesJar by creating(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }
    val javadocJar by creating(Jar::class) {
        archiveClassifier.set("javadoc")
        from(dokkaJavadoc)
        dependsOn(dokkaJavadoc)
    }
    artifacts {
        add("archives", sourcesJar)
        add("archives", javadocJar)
    }
    dokkaJavadoc {
        outputDirectory.set(file("$buildDir/javadoc"))
        dokkaSourceSets.configureEach {
            reportUndocumented.set(false)
            jdkVersion.set(8)
            perPackageOption {
                matchingRegex.set(".*\\.internal($|\\.).*")
                suppress.set(true)
            }
        }
    }
    javadoc {
        enabled = false
    }
    named<Jar>("javadocJar").configure {
        from(dokkaJavadoc)
    }
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(kotlin("gradle-plugin-api"))
    implementation("net.pwall.json:json-kotlin-schema:0.40")
    implementation("net.pwall.json:json-kotlin-schema-codegen:0.91")
    implementation("net.pwall.json:jsonutil:5.1")
    implementation("net.pwall.json:json-pointer:2.4")
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit"))
}

publishing {
    publications {
        afterEvaluate {
            named<MavenPublication>("pluginMaven") {
                artifact(file("build/libs/${project.name}-${project.version}-sources.jar")) {
                    classifier = "sources"
                }
                artifact(file("build/libs/${project.name}-${project.version}-javadoc.jar")) {
                    classifier = "javadoc"
                }
                pom {
                    name.set(displayName)
                    url.set(projectURL)
                    description.set(project.description)
                    packaging = "jar"
                    scm {
                        connection.set("scm:git:git://github.com/pwall567/${project.name}.git")
                        url.set(projectURL)
                    }
                    licenses {
                        license {
                            name.set("The MIT License (MIT)")
                            url.set("http://opensource.org/licenses/MIT")
                        }
                    }
                    developers {
                        developer {
                            id.set("pwall@pwall.net")
                            name.set("Peter Wall")
                            email.set("pwall@pwall.net")
                            url.set("https://pwall.net")
                            roles.set(setOf("architect", "developer"))
                            timezone.set("Australia/Sydney")
                        }
                    }
                }
            }
        }
    }
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(project.findProperty("ossrhUsername").toString())
            password.set(project.findProperty("ossrhPassword").toString())
        }
    }
}

signing {
    afterEvaluate {
        useGpgCmd()
        sign(*publishing.publications.toTypedArray())
    }
}
