package org.example.collision

import collision.CollisionQuadrants
import collision.ICheckHandler
import command.ICommand
import ioc.Ioc

class CollisionCheckHandler(
    private val collisionQuadrants: CollisionQuadrants,
    override val next: CollisionCheckHandler? = null
) : ICheckHandler {

    override fun handle(objId: String) {
        val checkCmd = Ioc.resolve<ICommand>(
            dependencyName = "Установить проверки коллизий",
            args = arrayOf(collisionQuadrants, objId)
        )
        checkCmd.execute()
        next?.handle(objId)
    }
}
