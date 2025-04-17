package spring.cmd

import command.IValueCommand
import spring.GameId
import spring.event.loop.IGameLoop

class GetGameCmd(
    private val gameId: String,
    private val gameMap: MutableMap<GameId, IGameLoop>,
) : IValueCommand<IGameLoop> {

    override fun execute(): IGameLoop {
        return gameMap[GameId(gameId)] ?: throw RuntimeException("Игра с id=$gameId не найдена")
    }
}