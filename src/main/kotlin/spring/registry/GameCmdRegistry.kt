package spring.registry

import command.ICommand
import ioc.Ioc
import motion.Vector
import spring.GameId
import spring.ObjId
import spring.cmd.*
import spring.event.loop.IGameLoop

typealias UniObj = MutableMap<String, Any>

class GameCmdRegistry {

    companion object {

        val objMap = mutableMapOf<ObjId, UniObj>()

        val gameMap = mutableMapOf<GameId, IGameLoop>()

        private val registrationCommands = listOf(
            Ioc.resolve<ICommand>(
                dependencyName = "Scopes.New",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Добавить игру", { params: Array<out Any> ->
                    RegisterGameCmd(
                        gameId = params[0] as String,
                        gameMap = gameMap,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Игра", { params: Array<out Any> ->
                    GetGameCmd(
                        gameId = params[0] as String,
                        gameMap = gameMap,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Добавить команду в игровой цикл", { params: Array<out Any> ->
                    AddCommandToGameLoopCmd(
                        gameId = params[0] as String,
                        command = params[1] as ICommand,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Добавить игровой объект", { params: Array<out Any> ->
                    val objId = params[0] as String
                    val obj = params[1] as UniObj
                    RegisterObjCmd(
                        objMap = objMap,
                        objId = ObjId(objId),
                        obj = obj,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Игровой объект", { params: Array<out Any> ->
                    val objId = params[0] as String
                    GetObjCmd(
                        objMap = objMap,
                        objId = ObjId(objId),
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Установить скорость", { params: Array<out Any> ->
                    SetVelocityCmd(
                        obj = params[0] as UniObj,
                        velocity = params[1] as Vector
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Движение по прямой", { params: Array<out Any> ->
                    RotationCmd(
                        obj = params[0] as UniObj,
                        args = params[1] as MutableMap<String, Any>,
                    )
                })
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Поворот", { params: Array<out Any> ->
                    RotationCmd(
                        obj = params[0] as UniObj,
                        args = params[1] as MutableMap<String, Any>,
                    )
                })
            ),
        )

        fun init() {
            registrationCommands.forEach { it.execute() }
        }
    }
}
