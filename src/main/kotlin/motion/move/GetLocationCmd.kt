package motion.move

import command.IValueCommand
import motion.Point

class GetLocationCmd(
    private val obj: MutableMap<String, Any>,
) : IValueCommand<Point> {

    override fun execute(): Point {
        return obj["location"] as Point
    }
}