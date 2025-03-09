package ioc

import org.example.command.ICommand
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.Angle
import motion.IUniversalObject
import motion.Point
import motion.Vector
import motion.move.MovableAdapter
import motion.move.MoveCmd
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class IocTest {

    @Test
    fun `register command in default scope, than resolve and execute it successfully`() {
        // Arrange
        val initialLocationMock = mockk<Point>().also {
            every { it.x } returns 1
            every { it.y } returns 1
        }
        val initialAngleMock = mockk<Angle>().also {
            every { it.getDegrees() } returns 1.1
        }
        val initialVelocityMock = mockk<Vector>().also {
            every { it.abs } returns 1
            every { it.angle } returns initialAngleMock
        }
        val uniObjMock = mockk<IUniversalObject>(relaxed = true).also {
            every { it.getProperty("location") }.returns(initialLocationMock)
            every { it.getProperty("velocity") }.returns(initialVelocityMock)
        }

        // Act
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("default")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Ioc.Register",
            args = arrayOf(
                "Движение по прямой",
                { params: Array<out Any> ->
                    val uniObj = params[0] as IUniversalObject
                    val moveable = MovableAdapter(obj = uniObj)
                    MoveCmd(obj = moveable)
                })
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Движение по прямой",
            args = arrayOf(uniObjMock)
        ).execute()

        // Assert
        verify { uniObjMock.setProperty("location" to Point(x = 2, y = 1)) }
    }

    @Test
    fun `execute command in new scope, than execute comand in default scope`() {
        // Arrange command_1 in default scope
        val initialLocationMock1 = mockk<Point>().also {
            every { it.x } returns 1
            every { it.y } returns 1
        }
        val initialAngleMock1 = mockk<Angle>().also {
            every { it.getDegrees() } returns 0.0
        }
        val velocityMock1 = mockk<Vector>().also {
            every { it.abs } returns 1
            every { it.angle } returns initialAngleMock1
        }
        val uniObjMock1 = mockk<IUniversalObject>(relaxed = true).also {
            every { it.getProperty("location") }.returns(initialLocationMock1)
            every { it.getProperty("velocity") }.returns(velocityMock1)
        }

        // Arrange command_2 in new scope
        val initialLocationMock2 = mockk<Point>().also {
            every { it.x } returns 2
            every { it.y } returns 2
        }
        val initialAngleMock2 = mockk<Angle>().also {
            every { it.getDegrees() } returns 0.0
        }
        val velocityMock2 = mockk<Vector>().also {
            every { it.abs } returns 2
            every { it.angle } returns initialAngleMock2
        }
        val uniObjMock2 = mockk<IUniversalObject>(relaxed = true).also {
            every { it.getProperty("location") }.returns(initialLocationMock2)
            every { it.getProperty("velocity") }.returns(velocityMock2)
        }

        // Act
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("default")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Ioc.Register",
            args = arrayOf(
                "Движение по прямой",
                { params: Array<out Any> ->
                    val uniObj = params[0] as IUniversalObject
                    val moveable = MovableAdapter(obj = uniObj)
                    MoveCmd(obj = moveable)
                })
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.New",
            args = arrayOf("scope2")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("scope2")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Ioc.Register",
            args = arrayOf(
                "Движение по прямой",
                { params: Array<out Any> ->
                    val uniObj = params[0] as IUniversalObject
                    val moveable = MovableAdapter(obj = uniObj)
                    MoveCmd(obj = moveable)
                })
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Движение по прямой",
            args = arrayOf(uniObjMock2)
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("default")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Движение по прямой",
            args = arrayOf(uniObjMock1)
        ).execute()

        // Assert
        verify { uniObjMock1.setProperty("location" to Point(x = 2, y = 1)) }
        verify { uniObjMock2.setProperty("location" to Point(x = 4, y = 2)) }
    }

    @Test
    fun `register and execute commands in 2 threads`() {
        // Arrange command in thread_1
        val initialLocationMock1 = mockk<Point>().also {
            every { it.x } returns 1
            every { it.y } returns 1
        }
        val initialAngleMock1 = mockk<Angle>().also {
            every { it.getDegrees() } returns 0.0
        }
        val velocityMock1 = mockk<Vector>().also {
            every { it.abs } returns 1
            every { it.angle } returns initialAngleMock1
        }
        val uniObjMock1 = mockk<IUniversalObject>(relaxed = true).also {
            every { it.getProperty("location") }.returns(initialLocationMock1)
            every { it.getProperty("velocity") }.returns(velocityMock1)
        }
        val thread1 = Thread {
            Ioc.currentScope = mutableMapOf<String, (params: Array<out Any>) -> Any>()
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf(
                    "Движение по прямой",
                    { params: Array<out Any> ->
                        val uniObj = params[0] as IUniversalObject
                        val moveable = MovableAdapter(obj = uniObj)
                        MoveCmd(obj = moveable)
                    })
            ).execute()

            Ioc.resolve<ICommand>(
                dependencyName = "Движение по прямой",
                args = arrayOf(uniObjMock1)
            ).execute()
        }

        // Arrange command in thread_2
        val initialLocationMock2 = mockk<Point>().also {
            every { it.x } returns 2
            every { it.y } returns 2
        }
        val initialAngleMock2 = mockk<Angle>().also {
            every { it.getDegrees() } returns 0.0
        }
        val velocityMock2 = mockk<Vector>().also {
            every { it.abs } returns 2
            every { it.angle } returns initialAngleMock2
        }
        val uniObjMock2 = mockk<IUniversalObject>(relaxed = true).also {
            every { it.getProperty("location") }.returns(initialLocationMock2)
            every { it.getProperty("velocity") }.returns(velocityMock2)
        }

        val thread2 = Thread {
            Ioc.currentScope = mutableMapOf<String, (params: Array<out Any>) -> Any>()
            Ioc.resolve<ICommand>(
                dependencyName = "Ioc.Register",
                args = arrayOf(
                    "Движение по прямой",
                    { params: Array<out Any> ->
                        val uniObj = params[0] as IUniversalObject
                        val moveable = MovableAdapter(obj = uniObj)
                        MoveCmd(obj = moveable)
                    })
            ).execute()

            Ioc.resolve<ICommand>(
                dependencyName = "Движение по прямой",
                args = arrayOf(uniObjMock2)
            ).execute()
        }

        // Act
        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

        // Assert
        verify { uniObjMock1.setProperty("location" to Point(x = 2, y = 1)) }
        verify { uniObjMock2.setProperty("location" to Point(x = 4, y = 2)) }
    }
}