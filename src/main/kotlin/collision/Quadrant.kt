package collision

import command.ICommand
import spring.ObjId

/**
 * Квадрат игрового поля (окрестность). Идентифицируется координатами: колонка (x), строка (y)
 */
data class Quadrant(
    // Хранит перечень объектов в окрестности
    val objs: MutableSet<ObjId> = mutableSetOf(),
    // Хранит макрокоманду проверки коллизий для каждого объекта в окрестности
    val checkCollisionCommands: MutableMap<ObjId, ICommand> = mutableMapOf()
)