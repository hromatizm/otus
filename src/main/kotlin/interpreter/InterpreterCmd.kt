package interpreter

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import spring.ObjId
import spring.event.loop.IGameLoop
import spring.registry.UniObj

class InterpreterCmd(
    val order: UniObj
) : IValueCommand<ICommand> {

    override fun execute(): ICommand {
        val gameLoop = getGameLoop()
        // Если объекта нет, то создаем пустую мапу для передачи в команду,
        // чтобы выполнять команды не только для игровых объектов, но и любые другие
        val obj = getGameObj() ?: mutableMapOf()
        obj["gameLoop"] = gameLoop
        val cmdArgs = order.toList()
        obj.putAll(cmdArgs)
        val actionCmd = getActionCmd(uniObj = obj)
        gameLoop.addEvent(command = actionCmd)
        return actionCmd
    }

    private fun getGameLoop(): IGameLoop {
        val gameId = order["gameId"] as String
        switchToScope(gameId)                       // Евент-луп берем из скоупа игры
        return Ioc.resolve<IValueCommand<IGameLoop>>(
            dependencyName = "Игра",
            args = arrayOf(gameId)
        ).execute()
    }

    private fun getGameObj(): UniObj? {
        val obj = runCatching {
            val userId = order["userId"] as String
            val objId = order["objId"] as String
            val obj = Ioc.resolve<IValueCommand<UniObj>>(
                dependencyName = "Получить игровой объект",
                args = arrayOf(userId, ObjId(objId))
            ).execute()
            obj
        }.getOrNull()
        return obj
    }

    private fun getActionCmd(uniObj: UniObj): ICommand {
        val gameId = order["gameId"] as String
        switchToScope(gameId)
        val actionCode = order["action"] as String           // Команду берем из скоупа игры
        return Ioc.resolve<ICommand>(
            dependencyName = actionCode,
            args = arrayOf(uniObj)
        )
    }

    private fun switchToScope(scopeId: String) {
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf(scopeId)
        ).execute()
    }

}
