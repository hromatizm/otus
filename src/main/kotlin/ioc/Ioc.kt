package ioc

import command.ICommand

class Ioc {

    companion object {

        private val scopeMap = mutableMapOf<String, MutableMap<String, (params: Array<out Any>) -> Any>>(
            "default" to mutableMapOf<String, (params: Array<out Any>) -> Any>()
        )

        private var defaultScope = scopeMap["default"]
        private val threadLocalScope = ThreadLocal<MutableMap<String, (params: Array<out Any>) -> Any>>()
        var currentScope: MutableMap<String, (params: Array<out Any>) -> Any>?
            get() = threadLocalScope.get()
            set(value) {
                threadLocalScope.set(value)
            }

        init {
            currentScope = defaultScope
            defaultScope?.set("Ioc.Register") { params ->
                object : ICommand {
                    override fun execute() {
                        currentScope?.set(params[0] as String, params[1] as (Array<out Any>) -> Any)
                    }
                }
            }

            defaultScope?.set("Scopes.New") { params ->
                object : ICommand {
                    override fun execute() {
                        scopeMap[params[0] as String] = mutableMapOf<String, (params: Array<out Any>) -> Any>()
                        val newScope = scopeMap[params[0] as String]
                        newScope?.set("Ioc.Register", { params ->
                            object : ICommand {
                                override fun execute() {
                                    newScope[params[0] as String] = params[1] as (Array<out Any>) -> Any
                                }
                            }
                        })
                    }
                }
            }

            defaultScope?.set("Scopes.Current") { params ->
                object : ICommand {
                    override fun execute() {
                        currentScope = scopeMap[params[0] as String]
                    }
                }
            }

        }

        fun <T> resolve(dependencyName: String, vararg args: Any): T {
            val resolveAction = currentScope?.get(dependencyName) ?: defaultScope?.get(dependencyName)
            return resolveAction?.invoke(args) as T
        }
    }
}