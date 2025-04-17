package spring.cmd

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import spring.event.loop.IGameLoop

class AddCommandToGameLoopCmd(
    private val gameId: String,
    private val command: ICommand,
) : ICommand {

    override fun execute() {
        val gameLoop = Ioc.resolve<IValueCommand<IGameLoop>>(
            dependencyName = "Игра",
            args = arrayOf(gameId)
        ).execute()
        gameLoop.addEvent(command)
    }
}