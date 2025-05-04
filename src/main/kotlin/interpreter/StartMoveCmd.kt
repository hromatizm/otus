package interpreter

import command.ICommand
import motion.Angle
import motion.Point
import motion.Vector
import spring.event.loop.IGameLoop
import spring.registry.UniObj
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

class StartMoveCmd(
    val obj: UniObj,
) : ICommand {

    override fun execute() {
        val location = obj["location"] as Point
        val velocity = getVelocity()
        val newLocation: Point = changeLocation(
            location = location,
            vector = velocity
        )
        obj["location"] = newLocation
        val gameLoop = obj["gameLoop"] as IGameLoop
        // После выполнения команда еще раз добавляется в евент-луп
        // Если скорость будет обнулена (например командой остановки движения),
        // то добавление команды движения в евент-луп прекратится
        if (velocity.abs > 0) {
            gameLoop.addEvent(command = this)
        }
    }

    private fun getVelocity(): Vector {
        val velocityAbs = obj["velocity"] as Int
        val velocityAngle = obj["angle"] as Double
        val velocity = Vector(
            abs = velocityAbs,
            angle = Angle(degrees = velocityAngle)
        )
        return velocity
    }

    private fun changeLocation(location: Point, vector: Vector): Point {
        val angle: Double = Math.toRadians(vector.angle.getDegrees())
        val deltaX: Int = (vector.abs * cos(angle)).roundToInt()
        val deltaY: Int = (vector.abs * sin(angle)).roundToInt()
        return Point(
            x = location.x + deltaX,
            y = location.y + deltaY,
        )
    }

}
