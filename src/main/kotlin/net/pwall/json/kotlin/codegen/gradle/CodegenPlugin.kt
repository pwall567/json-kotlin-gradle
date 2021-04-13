package net.pwall.json.kotlin.codegen.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class CodegenPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.tasks.create("codegen", CodegenTask::class.java)
    }

}
