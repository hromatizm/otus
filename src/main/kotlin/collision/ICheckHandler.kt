package collision

import org.example.collision.CollisionCheckHandler
import spring.registry.UniObj

interface ICheckHandler {

    val next: CollisionCheckHandler?

    fun handle(obj: UniObj)
}
