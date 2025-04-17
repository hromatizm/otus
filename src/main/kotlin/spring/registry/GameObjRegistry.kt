package org.example.spring.registry

import command.ICommand
import ioc.Ioc
import motion.Angle
import motion.Point
import motion.Vector

typealias UniObj = MutableMap<String, Any>

class GameObjRegistry {

    companion object {

        private val commands = listOf(
            Ioc.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Добавить игру",
                args = arrayOf("game_1")
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Добавить игровой объект",
                args = arrayOf(
                    "obj_1",
                    mutableMapOf(
                        "location" to Point(x = 1, y = 1),
                        "velocity" to Vector(abs = 1, angle = Angle(0.0))
                    )
                )
            ),
            Ioc.resolve<ICommand>(
                dependencyName = "Добавить игровой объект",
                args = arrayOf(
                    "obj_2",
                    mutableMapOf(
                        "location" to Point(x = 1, y = 1),
                        "velocity" to Vector(abs = 1, angle = Angle(0.0))
                    )
                )
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}
