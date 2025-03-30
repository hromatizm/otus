package motion.velocity

import motion.Vector

interface IObjWithChangeableVelocity {

    fun getVelocity(): Vector

    fun setVelocity(newVelocity: Vector)
}