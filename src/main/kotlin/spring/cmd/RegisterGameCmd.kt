package spring.cmd

import command.ICommand
import spring.GameId
import spring.event.loop.GameLoop
import spring.event.loop.IGameLoop
import java.util.concurrent.LinkedBlockingDeque

class RegisterGameCmd(
    private val gameId: String,
    private val gameMap: MutableMap<GameId, IGameLoop>,
) : ICommand {

    override fun execute() {
        val gameLoop = GameLoop(
            deque = LinkedBlockingDeque(),
            gameId = gameId
        )
        gameMap[GameId(gameId)] = gameLoop
        gameLoop.start()
    }
}