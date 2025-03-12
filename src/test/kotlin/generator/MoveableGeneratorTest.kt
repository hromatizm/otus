package generator

import command.IValueCommand
import ioc.Ioc
import motion.Angle
import motion.Point
import motion.Vector
import motion.move.IMoveable
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class MoveableGeneratorTest {

    @Test
    fun `generate adapter, than get velocity successfully`() {
        // Arrange
        val obj = mutableMapOf<String, Any>()
        val objVelocity = Vector(
            abs = 1,
            angle = Angle(degrees = 1.1)
        )
        obj["velocity"] = objVelocity

        // Act
        val adapter = Ioc.resolve<IValueCommand<IMoveable>>(
            dependencyName = "Adapter",
            args = arrayOf(IMoveable::class.java, obj)
        ).execute()
        val adapterVelocity = adapter.getVelocity()

        // Assert
        assertThat(adapterVelocity).isEqualTo(objVelocity)
    }

    @Test
    fun `generate adapter, than get location successfully`() {
        // Arrange
        val obj = mutableMapOf<String, Any>()
        val objLocation = Point(x = 1, y = 1)
        obj["location"] = objLocation

        // Act
        val adapter = Ioc.resolve<IValueCommand<IMoveable>>(
            dependencyName = "Adapter",
            args = arrayOf(IMoveable::class.java, obj)
        ).execute()
        val adapterLocation = adapter.getLocation()

        // Assert
        assertThat(adapterLocation).isEqualTo(objLocation)
    }

    @Test
    fun `generate adapter, than set new location successfully`() {
        // Arrange
        val obj = mutableMapOf<String, Any>()
        obj["location"] = Point(x = 1, y = 1)
        val newLocation = Point(x = 10, y = 20)

        // Act
        val adapter = Ioc.resolve<IValueCommand<IMoveable>>(
            dependencyName = "Adapter",
            args = arrayOf(IMoveable::class.java, obj)
        ).execute()
        adapter.setLocation(newLocation)

        // Assert
        assertThat(obj["location"]).isEqualTo(newLocation)
    }
}