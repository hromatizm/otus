package collision

import motion.Point
import spring.ObjId
import spring.registry.UniObj

class CollisionQuadrants(
    private val battleField: BattleField,
    private val shift: Int = 0,         // смещение окрестностей
) {
    private val quadrantSize: Int = 10 // размер окрестности (кол-во точек в стороне квадрата)
    private val cols = battleField.width / quadrantSize
    private val rows = battleField.height / quadrantSize

    /**
     *  Двумерный массив, хранит окрестности
     */
    private val field: List<MutableList<Quadrant>> =
        List(cols) {                             // координата окрестности по оси X
            MutableList(rows) { Quadrant() }     // координата окрестности по оси Y
        }

    /**
     * Для каждого объекта хранит список окрестностей, в которых он находится
     */
    private val objsQuadrants = mutableMapOf<ObjId, List<Quadrant>>()


    /**
     * Возвращает перечень объектов из окрестностей, в которые попал объект
     */
    fun getNeighbors(obj: UniObj): Map<Quadrant, Set<ObjId>> {
        val objId = ObjId(obj["id"] as String)
        removeFromField(objId)
        val objQuadrants: List<Quadrant> = putOnField(objId)
        val neighbors = findNeighbors(objQuadrants = objQuadrants, objId = objId)
        return neighbors
    }

    private fun removeFromField(objId: ObjId) {
        val previousObjQuadrants = objsQuadrants[objId]
        previousObjQuadrants?.forEach {
            it.objs.remove(objId)               // удаляем объект из окрестности
            it.checkCollisionCommands.remove(objId) // удаляем предыдущую макро команду проверки коллизий
        }
    }

    private fun putOnField(objId: ObjId): List<Quadrant> {
        val positionHolder = battleField.getPosition(objId)
        val newQuadrants: List<Quadrant> = positionHolder?.let {
            putObjPointsOnField(it)
        }.orEmpty()
        objsQuadrants[objId] = newQuadrants
        return newQuadrants
    }

    private fun putObjPointsOnField(positionHolder: ObjPositionHolder): List<Quadrant> {
        val objId = positionHolder.objId
        return listOf(
            positionHolder.leftTop,
            positionHolder.rightTop,
            positionHolder.rightBottom,
            positionHolder.leftBottom
        ).map { point -> putObjPointToField(objId, point) }
    }

    private fun putObjPointToField(objId: ObjId, point: Point): Quadrant {
        val col = (point.x + shift) / quadrantSize
        val row = (point.y + shift) / quadrantSize
        val quadrant = field[col][row]
        quadrant.objs.add(objId)
        return quadrant
    }

    /**
     * Возвращает список объектов для каждой окрестности
     */
    private fun findNeighbors(objQuadrants: List<Quadrant>, objId: ObjId): Map<Quadrant, Set<ObjId>> {
        val resultMap = objQuadrants.associateWith { quadrant ->
            val neighbors = quadrant.objs
            neighbors.remove(objId) // сам объект не является соседом
            neighbors
        }
        return resultMap
    }
}
