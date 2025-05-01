package collision

import motion.Point
import spring.ObjId

class ObjPositionHolder(
    val objId: ObjId,
    // крайние точки объекта, которые определяют его положение на плоскости с учетом размера объекта
    val leftTop: Point,
    val rightTop: Point,
    val rightBottom: Point,
    val leftBottom: Point
)