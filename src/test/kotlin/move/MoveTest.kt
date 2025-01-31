package move

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import org.assertj.core.api.Assertions.assertThat
import org.example.move.*
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import kotlin.test.Test

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockKExtension::class)
class MoveTest {

    private val testingMove = Move()

    @Test
    fun `move correctly with vector (-7, 3)`() {
        // Arrange
        val angleMock = mockk<Angle>().also {
            every { it.degrees } returns 156.8
        }
        val velocityMock = mockk<Vector>().also { //  vector (-7, 3)
            every { it.angle } returns angleMock
            every { it.abs } returns 8
        }
        val initialLocationMock = mockk<Point>().also {
            every { it.x } returns 12
            every { it.y } returns 5
        }
        val movingObjMock = mockk<IMoveable>(relaxed = true).also {
            every { it.getVelocity() } returns velocityMock
            every { it.getLocation() } returns initialLocationMock
        }

        // Act
        testingMove.execute(movingObjMock)

        // Assert
        verify { movingObjMock.getVelocity() }
        verify { movingObjMock.getLocation() }
        verify {
            movingObjMock.setLocation(
                newLocation = withArg {
                    assertThat(it).isEqualTo(Point(x = 5, y = 8))
                }
            )
        }
        confirmVerified(movingObjMock)
    }
}
