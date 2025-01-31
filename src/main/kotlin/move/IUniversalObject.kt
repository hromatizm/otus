package org.example.move

interface IUniversalObject {

    fun getProperty(name: String): Any?

    fun setProperty(nameToValue: Pair<String, Any>)
}
