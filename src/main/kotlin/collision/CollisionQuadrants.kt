package collision

import motion.Point
import spring.ObjId

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

    init {
        battleField.getObjs().forEach { objId ->
            putOnField(objId)
        }
    }

    /**
     * Получение копии списка окрестностей для тестов
     */
    fun getFieldCopy(): List<List<Quadrant>> {
        return field.map {
            it.map { quadrant -> quadrant.copy() }
        }
    }

    /**
     * Возвращает перечень объектов из окрестностей, в которые попал объект
     */
    fun setPosition(objId: ObjId) {
        removeFromField(objId)
        putOnField(objId)
    }

    private fun removeFromField(objId: ObjId) {
        val previousObjQuadrants = objsQuadrants[objId]
        previousObjQuadrants?.forEach {
            it.objs.remove(objId)               // удаляем объект из окрестности
            it.checkCollisionCommands.remove(objId) // удаляем предыдущую макро команду проверки коллизий
        }
    }

    private fun putOnField(objId: ObjId) {
        val positionHolder = battleField.getPosition(objId)
        val newQuadrants: List<Quadrant> = positionHolder?.let {
            putObjPointsOnField(it)
        }.orEmpty()
        objsQuadrants[objId] = newQuadrants
    }

    private fun putObjPointsOnField(positionHolder: ObjPositionHolder): List<Quadrant> {
        val objId = positionHolder.objId
        return listOf(
            positionHolder.leftTop,
            positionHolder.rightTop,
            positionHolder.rightBottom,
            positionHolder.leftBottom
        ).map { point -> putObjPointToField(objId, point) }.distinct()
    }

    private fun putObjPointToField(objId: ObjId, point: Point): Quadrant {
        val col = (point.x + shift) / quadrantSize
        val row = (point.y + shift) / quadrantSize
        val quadrant = field[col][row]
        quadrant.objs.add(objId)
        return quadrant
    }

    /**
     * Возвращает перечень объектов из окрестностей, в которые попал объект
     */
    fun findNeighbors(objId: ObjId): List<Pair<Quadrant, Set<ObjId>>> {
        val objQuadrants = objsQuadrants[objId]
        val result = objQuadrants?.map { quadrant ->
            val neighbors = quadrant.objs
            neighbors.remove(objId) // сам объект не является соседом
            quadrant to neighbors
        }?.filter { it.second.isNotEmpty() }
        return result.orEmpty()
    }
}
