package org.example.collision

import collision.CollisionQuadrants
import collision.SetCollisionCheckersCmd
import collision.ICheckHandler
import spring.registry.UniObj

class CollisionCheckHandler(
    private val collisionQuadrants: CollisionQuadrants,
    override val next: CollisionCheckHandler? = null
) : ICheckHandler {

   override fun handle(obj: UniObj) {
        val checkCmd = SetCollisionCheckersCmd(collisionQuadrants = collisionQuadrants, obj = obj)
        checkCmd.execute()
        next?.handle(obj)
    }
}
