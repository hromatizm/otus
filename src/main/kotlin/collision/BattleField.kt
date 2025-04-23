package org.example.collision

import command.IValueCommand
import ioc.Ioc
import motion.Point
import spring.ObjId
import spring.registry.UniObj

class BattleField {

    private val width = 100
    private val height = 100
    private val quadrantSize = 10

    private val cols = width / quadrantSize
    private val rows = height / quadrantSize
    private val locationMap = mutableMapOf<ObjId, ObjLocationHolder>()
    private val shift = 2

    private val field: List<List<MutableList<ObjId>>> =
        List(rows) {
            List(cols) {
                mutableListOf()
            }
        }

    /**
     * Возвращает команду проверки колизий (сама команда проверки вохвращает список объектов, с которыми произошла колизия)
     */
    fun moveWithCollisionCheck(obj: UniObj): IValueCommand<List<ObjId>> {
        removeFromField(obj)
        val objQuadrants: List<Quadrant> = putOnField(obj)
        val objId = obj["id"] as String
        val neighbors = findNeighbors(objQuadrants, ObjId(objId))
        val collisionCheckCmd = Ioc.resolve<IValueCommand<List<ObjId>>>(
            dependencyName = "Проверка коллизий",
            args = arrayOf(obj, neighbors)
        )
        return collisionCheckCmd
    }

    private fun removeFromField(obj: UniObj) {
        val objId = obj["id"] as String
        val locationHolder = locationMap[ObjId(objId)]
        locationHolder?.let { removeObjPointsFromField(it) }
    }

    private fun removeObjPointsFromField(locHolder: ObjLocationHolder){
        val objId = locHolder.objId
        removeObjPointFromField(objId = objId, point = locHolder.leftTop)
        removeObjPointFromField(objId = objId, point = locHolder.rightTop)
        removeObjPointFromField(objId = objId, point = locHolder.rightBottom)
        removeObjPointFromField(objId = objId, point = locHolder.leftBottom)
    }

    private fun removeObjPointFromField(objId: ObjId, point: Point) {
        val row = point.x / quadrantSize
        val col = point.y / quadrantSize
        field[row][col].remove(objId)
    }

    private fun putOnField(obj: UniObj): List<Quadrant> {
        val objId = obj["id"] as String
        val location = obj["location"] as Point // упрощающее ограничение: точка нахождения объекта - центр объекта
        val size = obj["size"] as Int // упрощающее ограничение: объекты квадратные с нечетным размером стороны
        val halfSize = size / 2
        val x = location.x
        val y = location.y
        val locHolder = ObjLocationHolder(
            objId = ObjId(objId),
            leftTop = Point(x = x - halfSize, y = y + halfSize),
            rightTop = Point(x = x + halfSize, y = y + halfSize),
            rightBottom = Point(x = x + halfSize, y = y - halfSize),
            leftBottom = Point(x = x - halfSize, y = y - halfSize),
        )
        locationMap[ObjId(objId)] = locHolder
        return putObjPointsToField(locHolder)
    }

    private fun putObjPointsToField(locHolder: ObjLocationHolder): List<Quadrant> {
        val objQuadrants = mutableListOf<Quadrant>()
        val objId = locHolder.objId
        objQuadrants += putObjPointToField(objId = objId, point = locHolder.leftTop)
        objQuadrants += putObjPointToField(objId = objId, point = locHolder.rightTop)
        objQuadrants += putObjPointToField(objId = objId, point = locHolder.rightBottom)
        objQuadrants += putObjPointToField(objId = objId, point = locHolder.leftBottom)
        return objQuadrants
    }

    private fun putObjPointToField(objId: ObjId, point: Point): Quadrant {
        val row = point.x / quadrantSize
        val col = point.y / quadrantSize
        field[row][col].add(objId)
        return Quadrant(row, col)
    }

    private fun findNeighbors(objQuadrants: List<Quadrant>, objId: ObjId): List<ObjId> {
        val neighbors = mutableListOf<ObjId>()
        for (quadrant in objQuadrants) {
            neighbors += field[quadrant.row][quadrant.col]
        }
        neighbors.remove(objId) // сам объект не является соседом
        return neighbors
    }

    private class ObjLocationHolder(
        val objId: ObjId,
        // крайние точки объекта, которые определяют его положение на плоскости с учетом размера объекта
        val leftTop: Point,
        val rightTop: Point,
        val rightBottom: Point,
        val leftBottom: Point
    )

    class Quadrant(
        val row: Int,
        val col: Int
    )
}