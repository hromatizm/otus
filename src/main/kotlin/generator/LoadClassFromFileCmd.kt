package org.example.generator

import command.IValueCommand
import java.io.File
import java.net.URLClassLoader

class LoadClassFromFileCmd(
    private val path: String,
    private val className: String,
) : IValueCommand<Class<*>> {

    override fun execute(): Class<*> {
        val dir = File(path)
        if (!dir.exists() || !dir.isDirectory) {
            throw IllegalArgumentException("Output directory $path is invalid")
        }
        val url = dir.toURI().toURL()
        val classLoader = URLClassLoader(arrayOf(url), Thread.currentThread().contextClassLoader)
        return classLoader.loadClass(className)
    }
}