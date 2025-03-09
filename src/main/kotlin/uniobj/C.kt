package org.example.uniobj
class GeneratedMoveableAdapter(
    val obj: MutableMap<String, Any>
) : motion.move.IMoveable {

    override fun getLocation(): motion.Point {
        return ioc.Ioc.resolve<motion.Point>(
            dependencyName = "motion.move.IMoveable:location.get",
            args = arrayOf(obj),
        )
    }

    override fun getVelocity(): motion.Vector {
        return ioc.Ioc.resolve<motion.Vector>(
            dependencyName = "motion.move.IMoveable:velocity.get",
            args = arrayOf(obj),
        )
    }

    override fun setLocation(arg0: motion.Point) {
        return ioc.Ioc.resolve<command.ICommand>(
            dependencyName = "motion.move.IMoveable:location.set",
            args = arrayOf(obj),
        ).execute()
    }
}