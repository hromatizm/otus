package spring.registry

import command.ICommand
import ioc.Ioc
import spring.cmd.InterpretCommand

class DefaultScopeRegistry {

    companion object {

        private val commands = listOf(
            Ioc.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("default")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Интерпретатор команд", { params: Array<out Any> ->
                    InterpretCommand(
                        gameOrderParams = params[0] as Map<String, String>
                    )
                })
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}
