package collision

import org.example.collision.CollisionCheckHandler

interface ICheckHandler {

    val next: CollisionCheckHandler?

    fun handle(objId: String)
}
