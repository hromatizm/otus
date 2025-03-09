package org.example.generator

import org.jetbrains.kotlin.cli.common.ExitCode
import org.jetbrains.kotlin.cli.jvm.K2JVMCompiler
import java.io.File
import java.net.URLClassLoader

class AdapterGenerator {

    private val s1 = """
        class GeneratedMovableAdapter(
            val obj: MutableMap<String, Any>
        ) : motion.move.IMoveable {
        
            override fun getVelocity(): motion.Vector {
                return ioc.Ioc.resolve<motion.Vector>(
                    dependencyName = "motion.move.IMoveable:velocity.get",
                    args = arrayOf(obj),
                )
            }
        
            override fun getLocation(): motion.Point {
                return ioc.Ioc.resolve<motion.Point>(
                    dependencyName = "motion.move.IMoveable:location.get",
                    args = arrayOf(obj),
                )
            }
        
            override fun setLocation(newValue: motion.Point) {
                ioc.Ioc.resolve<org.example.command.ICommand>(
                    dependencyName = "motion.move.IMoveable:location.set",
                    args = arrayOf(obj, newValue),
                ).execute()
            }
        }""".trimIndent()

    private val s2 = """
        class GeneratedMovableAdapter {
            fun getVelocity() {
                kotlin.io.println("UPS!!!")
            }
        }
    """.trimIndent()

    fun run() {
        val outputDir = "build"
        compileToClass(s1, outputDir)
//        val clazz = loadClassFromFile(outputDir, "GeneratedMovableAdapter")
//        createAndInvokeClass(clazz)
    }

    fun compileToClass(sourceCode: String, outputDir: String) {
        val sourceFile = File("$outputDir/GeneratedMovableAdapter.kt")
        val outputDirectory = File(outputDir)
        outputDirectory.mkdirs()
        sourceFile.writeText(sourceCode)
        val compilerArgs = arrayOf(
            sourceFile.absolutePath,
            "-d", outputDir,
            "-classpath", System.getProperty("java.class.path")
        )
        val compiler = K2JVMCompiler()
        val exitCode = compiler.exec(System.err, *compilerArgs)

        if (exitCode != ExitCode.OK) {
            throw RuntimeException("Kotlin compilation failed with exit code $exitCode")
        }
    }

    fun loadClassFromFile(outputDir: String, className: String): Class<*> {
        val outputDirectory = File(outputDir)
        if (!outputDirectory.exists() || !outputDirectory.isDirectory) {
            throw IllegalArgumentException("Output directory $outputDir is invalid")
        }
        val url = outputDirectory.toURI().toURL()
        val classLoader = URLClassLoader(arrayOf(url), this::class.java.classLoader)
        return classLoader.loadClass(className)
    }

    fun createAndInvokeClass(clazz: Class<*>) {
        try {
            val instance = clazz.getDeclaredConstructor().newInstance()
//            val getVelocityMethod = clazz.getDeclaredMethod("getVelocity")
//            getVelocityMethod.invoke(instance)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}