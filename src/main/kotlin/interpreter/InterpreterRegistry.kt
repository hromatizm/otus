package interpreter

import command.ICommand
import ioc.Ioc
import org.example.interpreter.AddObjCmd
import spring.ObjId
import spring.registry.UniObj

class InterpreterRegistry {

    companion object {

        private val commands = listOf(
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Scopes.New",
                args = arrayOf("user_9")
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Scopes.New",
                args = arrayOf("user_10")
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Новый игровой объект", { params: Array<out Any> ->
                    AddObjCmd(
                        userId = params[0] as String,
                        objId = params[1] as ObjId,
                        obj = params[2] as UniObj,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Получить игровой объект", { params: Array<out Any> ->
                    GetObjCmd(
                        userId = params[0] as String,
                        objId = params[1] as ObjId,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Начать движение", { params: Array<out Any> ->
                    StartMoveCmd(
                        obj = params[0] as UniObj,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Прекратить движение", { params: Array<out Any> ->
                    StopMoveCmd(
                        obj = params[0] as UniObj,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Выстрел", { params: Array<out Any> ->
                    FireCmd(
                        obj = params[0] as UniObj,
                    )
                })
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}