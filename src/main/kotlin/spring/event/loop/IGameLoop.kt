package spring.event.loop

import command.ICommand

interface IGameLoop {

    val gameId: String

    fun start()

    fun stop()

    fun addEvent(command: ICommand)
}
