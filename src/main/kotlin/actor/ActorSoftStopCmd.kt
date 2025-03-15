package org.example.actor

import actor.ActorRegistry
import command.ICommand

class ActorSoftStopCmd(
    private val actorName: String,
) : ICommand {

    override fun execute() {
        val actor = ActorRegistry.get(actorName) ?: return
        val previousBehavior = actor.behavior
        actor.behavior = {
            if (actor.deque.isNotEmpty()) {
                 previousBehavior.invoke()
            } else {
                actor.stop()
            }
        }
    }
}