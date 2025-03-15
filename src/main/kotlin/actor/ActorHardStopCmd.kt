package actor

import command.ICommand

class ActorHardStopCmd(
    private val actorName: String,
) : ICommand {

    override fun execute() {
        val actor = ActorRegistry.get(actorName)
        actor?.stop()
    }
}