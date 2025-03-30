package command

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import motion.Angle
import motion.Vector
import motion.rotate.IRotatable
import motion.macro.MacroCmd
import motion.rotate.RotationCmd
import motion.velocity.ChangeVelocityAngleCmd
import motion.velocity.IObjWithChangeableVelocity
import kotlin.test.Test

class RotateAndChangeVelocityAngleCmdTest {

    interface MacroInterface : IObjWithChangeableVelocity, IRotatable

    @Test
    fun `when rotate - execute rotation and execute change velocity angle`() {
        // Arrange
        val newAngleMock = mockk<Angle>(relaxed = true)
        val newVelocityMock = mockk<Vector>(relaxed = true)
        val objMock = mockk<MacroInterface>(relaxed = true)
        val rotationCmdMock = mockk<RotationCmd>(relaxed = true).also {
            every { it.execute() } answers { objMock.setAngle(newAngleMock) }
        }
        val changeVelocityAngleCmdMock = mockk<ChangeVelocityAngleCmd>(relaxed = true).also {
            every { it.execute() } answers { objMock.setVelocity(newVelocityMock) }
        }

        val testingCmd = MacroCmd(commands = listOf(rotationCmdMock, changeVelocityAngleCmdMock))

        // Act
        testingCmd.execute()

        // Assert
        verify { objMock.setAngle(newAngleMock) }
        verify { objMock.setVelocity(newVelocityMock) }
    }
}
