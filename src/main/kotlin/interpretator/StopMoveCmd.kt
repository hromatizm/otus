package interpretator

import command.ICommand
import motion.Angle
import motion.Vector
import spring.registry.UniObj

class StopMoveCmd(
    val obj: UniObj,
) : ICommand {

    override fun execute() {
        val velocityAngle = obj["angle"] as Double      // Угол скорости оставлям прежним
        obj["velocity"] = Vector(
            abs = 0,                                    // Моодуль скорости зануляем
            angle = Angle(degrees = velocityAngle)
        )
    }
}