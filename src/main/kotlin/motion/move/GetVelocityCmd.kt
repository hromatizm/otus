package motion.move

import command.IValueCommand
import motion.Vector

class GetVelocityCmd(
    private val obj: MutableMap<String, Any>,
) : IValueCommand<Vector> {

    override fun execute(): Vector {
        return obj["velocity"] as Vector
    }
}