package org.example.collision

import collision.CollisionCheckerCmd
import collision.CollisionQuadrants
import collision.SetCollisionCheckersCmd
import command.ICommand
import ioc.Ioc
import spring.ObjId
import spring.cmd.GetObjCmd
import spring.cmd.RegisterObjCmd
import spring.registry.UniObj

class CollisionRegistry {

    companion object {

        private val commands = listOf(
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Scopes.Current",
                args = arrayOf("game_1")
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Проверка коллизии", { params: Array<out Any> ->
                    CollisionCheckerCmd(
                        obj = params[0] as UniObj,
                        neighborId = params[1] as String
                    )
                })
            ),
            Ioc.Companion.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf("Установить проверки коллизий", { params: Array<out Any> ->
                    SetCollisionCheckersCmd(
                        collisionQuadrants = params[0] as CollisionQuadrants,
                        objId = params[1] as String
                    )
                })
            ),
        )

        fun init() {
            commands.forEach { it.execute() }
        }
    }
}