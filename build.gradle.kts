/*
 * @(#) build.gradle.kts
 */

@file:Suppress("UnstableApiUsage")

import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "net.pwall.json"
version = "0.0.1"
description = "Code Generation Plugin for JSON Schema"

val projectURL = "https://github.com/pwall567/${project.name}"

plugins {
    id("org.jetbrains.kotlin.jvm") version("1.4.20")
    id("org.jetbrains.dokka") version "1.4.30"
    id("io.github.gradle-nexus.publish-plugin") version("1.0.0")
    `kotlin-dsl`
    `java-gradle-plugin`
    `maven-publish`
    signing
}

repositories {
    jcenter()
}

tasks {
    withType<KotlinCompile>().configureEach {
        kotlinOptions {
            languageVersion = "1.4"
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
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("net.pwall.json:json-kotlin-schema-codegen:0.30")
    implementation("net.pwall.json:jsonutil:4.1")
    implementation("net.pwall.json:json-pointer:1.0")
    implementation("net.pwall.yaml:yaml-simple:1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

gradlePlugin {
    plugins {
        create("codegen") {
            id = "net.pwall.json.schema.codegen"
            implementationClass = "net.pwall.json.kotlin.codegen.gradle.CodegenPlugin"
        }
    }
}

signing {
    useGpgCmd()
    sign(configurations.archives.get())
}

nexusPublishing {
    repositories {
        sonatype {
            username.set(System.getenv("NEXUS_USERNAME"))
            password.set(System.getenv("NEXUS_PASSWORD"))
        }
    }
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            artifact(file("build/libs/${project.name}-${project.version}-sources.jar")) {
                classifier = "sources"
            }
            artifact(file("build/libs/${project.name}-${project.version}-javadoc.jar")) {
                classifier = "javadoc"
            }
            pom {
                name.set(project.description)
                url.set(projectURL)
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
