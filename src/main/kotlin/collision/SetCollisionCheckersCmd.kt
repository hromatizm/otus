package collision

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.macro.MacroCmd
import spring.ObjId
import spring.registry.UniObj

class SetCollisionCheckersCmd(
    private val collisionQuadrants: CollisionQuadrants,
    private val objId: String,
) : ICommand {

    /**
     * Помещает макро команду проверки колизий в каждую окрестность, где находится объект
     */
    override fun execute() {
        val obj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()
        val neighborQuadrants: List<Pair<Quadrant, Set<ObjId>>> = collisionQuadrants.findNeighbors(ObjId(objId))
        neighborQuadrants.onEach {
            val quadrant = it.first
            val neighborIds = it.second
            val checkCommands = neighborIds.map { objId ->
                Ioc.resolve<ICommand>(
                    dependencyName = "Проверка коллизии",
                    args = arrayOf(obj, objId.id)
                )
            }
            val objId = ObjId(obj["id"] as String)
            quadrant.checkCollisionCommands[objId] = MacroCmd(commands = checkCommands)
        }
    }

}
