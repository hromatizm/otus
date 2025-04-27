package collision

import command.ICommand
import ioc.Ioc
import spring.registry.UniObj

/**
 * Команда проверки коллизии между двумя игровыми объектами.
 * По ДЗ реализация не требуется.
 */
class CollisionCheckerCmd(
    private val obj: UniObj,
    private val neighborId: String,
) : ICommand {

    override fun execute() {
        val objId = obj["id"] as String
        val neighbor = Ioc.resolve<ICommand>(
            dependencyName = "Игровой объект",
            args = arrayOf(neighborId)
        )
        println("Проведена проверка коллизии между объектами $objId и $neighborId")
    }
}