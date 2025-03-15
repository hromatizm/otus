package actor

import java.util.concurrent.ConcurrentHashMap

class ActorRegistry {

    companion object {

        private val actorMap = ConcurrentHashMap<String, IActor>()

        fun register(actorName: String, actor: IActor) {
            actorMap[actorName] = actor
        }

        fun get(actorName: String): IActor? {
            return actorMap[actorName]
        }
    }
}
