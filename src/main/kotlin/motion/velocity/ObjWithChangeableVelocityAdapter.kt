package motion.velocity

import motion.IUniversalObject
import motion.Vector

class ObjWithChangeableVelocityAdapter(
    val obj: IUniversalObject,
) : IObjWithChangeableVelocity {

    override fun getVelocity(): Vector {
        return obj.getProperty("velocity") as Vector
    }

    override fun setVelocity(velocity: Vector) {
        obj.setProperty("velocity" to velocity)
    }
}