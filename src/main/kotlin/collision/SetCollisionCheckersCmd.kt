package collision

import command.ICommand
import ioc.Ioc
import motion.macro.MacroCmd
import spring.ObjId
import spring.registry.UniObj

class SetCollisionCheckersCmd(
    private val collisionQuadrants: CollisionQuadrants,
    private val obj: UniObj,
) : ICommand {

    /**
     * Помещает макро команду проверки колизий в каждую окрестность, где находится объект
     */
    override fun execute() {
        val neighborQuadrants: Map<Quadrant, Set<ObjId>> = collisionQuadrants.getNeighbors(obj)
        neighborQuadrants.forEach {
            val quadrant = it.key
            val neighbors = it.value
            val checkCommands = neighbors.map { neighbor ->
                Ioc.resolve<ICommand>(
                    dependencyName = "Проверка коллизи", args = arrayOf(obj, neighbor)
                )
            }
            val objId = ObjId(obj["id"] as String)
            quadrant.checkCollisionCommands[objId] = MacroCmd(commands = checkCommands)
        }
    }

}
