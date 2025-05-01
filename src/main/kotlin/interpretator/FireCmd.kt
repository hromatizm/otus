package interpretator

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.Angle
import motion.Vector
import spring.registry.UniObj
import java.util.*

/**
 * Выстрел реализуется как создание двигающегося объекта 'снаряд', который начинет движение через команду StartMove
 */
class FireCmd(
    val obj: UniObj,
) : ICommand {

    override fun execute() {
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf(obj["userId"] as String)         // объект 'снаряд' добавляем в скоуп игрока
        ).execute()
        val armId = UUID.randomUUID().toString()
        val arm = mutableMapOf(
            "objId" to armId,
            "userId" to obj["userId"],
            "angle" to obj["angle"],
            "location" to obj["location"],
            "velocity" to getArmVelocity(),
            "gameLoop" to obj["gameLoop"],
        )
        Ioc.resolve<ICommand>(
            dependencyName = "Добавить игровой объект",
            args = arrayOf(armId, arm)
        ).execute()
        Ioc.resolve<ICommand>(
            dependencyName = "Начать движение",
            args = arrayOf(arm)
        ).execute()
    }

    private fun getArmVelocity(): Vector {
        val velocityAbs = obj["armVelocity"] as Int
        val velocityAngle = obj["angle"] as Double
        val velocity = Vector(
            abs = velocityAbs,
            angle = Angle(degrees = velocityAngle)
        )
        return velocity
    }

}