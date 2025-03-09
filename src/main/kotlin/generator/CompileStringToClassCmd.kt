package org.example.generator

import command.ICommand
import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File

class CompileStringToClassCmd(
    private val sourceCode: String,
    private val className: String,
    private val outputDir: String
) : ICommand {

    override fun execute() {
        val targetFile = File("$outputDir/$className.kt")
        File(outputDir).mkdirs()
        targetFile.writeText(sourceCode)
        val compilerArgs = arrayOf(
            targetFile.absolutePath,
            "-d", outputDir,
            "-classpath", System.getProperty("java.class.path")
        )
        val compiler = K2JVMCompiler()
        val exitCode = compiler.exec(System.err, *compilerArgs)
        if (exitCode != ExitCode.OK) {
            throw RuntimeException("String to class compilation failed with exit code $exitCode")
        }
    }
}