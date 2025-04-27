package collision

import motion.Point
import spring.ObjId
import spring.registry.UniObj

class BattleField(
    val width: Int = 100,
    val height: Int = 100,
) {

    /**
     * Мапа содержит положение каждого объекта.
     * Положение определяется через ObjLocationHolder,
     * который хранит для объекта его 4 крайние точки,
     * через которые определяется, в какие окрестности попал объект.
     */
    private val locationMap = mutableMapOf<ObjId, ObjPositionHolder>()

    fun getPosition(objId: ObjId): ObjPositionHolder? {
        return locationMap[objId]
    }

    fun changePosition(obj: UniObj) {
        val objId = obj["id"] as String
        val location = obj["location"] as Point     // упрощающее ограничение: точка нахождения объекта - центр объекта
        val size = obj["size"] as Int               // упрощающее ограничение: объекты квадратные с нечетным размером стороны
        val halfSize = size / 2
        val x = location.x
        val y = location.y
        val locHolder = ObjPositionHolder(
            objId = ObjId(objId),
            leftTop = Point(x = x - halfSize, y = y + halfSize),
            rightTop = Point(x = x + halfSize, y = y + halfSize),
            rightBottom = Point(x = x + halfSize, y = y - halfSize),
            leftBottom = Point(x = x - halfSize, y = y - halfSize),
        )
        locationMap[ObjId(objId)] = locHolder
    }
}
