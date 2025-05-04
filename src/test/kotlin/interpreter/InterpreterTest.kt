package interpreter

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.Point
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility.await
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import spring.ObjId
import spring.event.loop.IGameLoop
import spring.registry.UniObj
import java.util.concurrent.TimeUnit

@TestInstance(TestInstance.Lifecycle.PER_METHOD)
class InterpreterTest {

    @BeforeEach
    fun setScope() {
        Ioc.Companion.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("game_1")
        ).execute()
    }

    @Test
    fun `after interpreting start move command obj should change location`() {
        // Arrange
        Ioc.resolve<ICommand>(
            dependencyName = "Новый игровой объект",
            args = arrayOf(
                "user_9",
                ObjId("obj_9"),
                mutableMapOf(
                    "id" to "obj_9",
                    "location" to Point(x = 1, y = 1), // изначально объект находится в точке 1 - 1
                    "velocity" to 1,
                    "angle" to 0.0
                )
            )
        ).execute()

        val testingCmd = InterpreterCmd(
            order = mapOf(
                "gameId" to "game_1",
                "userId" to "user_9",
                "objId" to "obj_9",
                "action" to "Начать движение"
            ) as UniObj
        )
        // Act
        val resultCmd = testingCmd.execute()

        // Assert
        val obj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Получить игровой объект",
            args = arrayOf("user_9", ObjId("obj_9"))
        ).execute()

        assertThat(resultCmd).isExactlyInstanceOf(StartMoveCmd::class.java)

        await().atMost(10, TimeUnit.SECONDS).untilAsserted {
            val newLocation = obj["location"] as Point
            assertThat(newLocation.x).isGreaterThan(1)  // Убеждаемся, что движение начато
        }
        await().atMost(10, TimeUnit.SECONDS).untilAsserted {
            val newLocation = obj["location"] as Point
            assertThat(newLocation.x).isGreaterThan(10)  // Убеждаемся, что движение продолжается
        }
    }

    @Test
    fun `after interpreting stop move command obj should stop changing location`() {
        // Arrange
        Ioc.resolve<ICommand>(
            dependencyName = "Новый игровой объект",
            args = arrayOf(
                "user_9",
                ObjId("obj_9"),
                mutableMapOf(
                    "id" to "obj_9",
                    "location" to Point(x = 1, y = 1), // изначально объект находится в точке 1 - 1
                    "velocity" to 1,
                    "angle" to 0.0
                )
            )
        ).execute()

        val startMoveCmd = InterpreterCmd(
            order = mapOf(
                "gameId" to "game_1",
                "userId" to "user_9",
                "objId" to "obj_9",
                "action" to "Начать движение"
            ) as UniObj
        )
        val testingCmd = InterpreterCmd(
            order = mapOf(
                "gameId" to "game_1",
                "userId" to "user_9",
                "objId" to "obj_9",
                "action" to "Прекратить движение"
            ) as UniObj
        )

        // Act
        startMoveCmd.execute()
        Thread.sleep(100)
        val resultCmd = testingCmd.execute()
        Thread.sleep(100)

        // Assert
        val obj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Получить игровой объект",
            args = arrayOf("user_9", ObjId("obj_9"))
        ).execute()

        val locationBeforeSleep = (obj["location"] as Point).copy()
        Thread.sleep(1_000)
        val locationAfterSleep = obj["location"] as Point

        assertThat(resultCmd).isExactlyInstanceOf(StopMoveCmd::class.java)
        assertThat(locationBeforeSleep).isEqualTo(locationAfterSleep)  // Убеждаемся, что движение остановлено
    }

    @Test
    fun `interpret fire command`() {
        // Arrange
        Ioc.resolve<ICommand>(
            dependencyName = "Новый игровой объект",
            args = arrayOf(
                "user_9",
                ObjId("obj_9"),
                mutableMapOf(
                    "id" to "obj_9",
                    "location" to Point(x = 1, y = 1),
                    "velocity" to 1,
                    "angle" to 0.0,
                    "armVelocity" to 10,
                )
            )
        ).execute()

        val testingCmd = InterpreterCmd(
            order = mapOf(
                "gameId" to "game_1",
                "userId" to "user_10", // другой пользователь
                "objId" to "obj_9",
                "action" to "Выстрел"
            ) as UniObj
        )
        Ioc.Companion.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("game_1")
        ).execute()
        val gameLoop = Ioc.resolve<IValueCommand<IGameLoop>>(
            dependencyName = "Игра",
            args = arrayOf("game_1")
        ).execute()
        gameLoop.stop()

        // Act
        val resultCmd = testingCmd.execute()

        // Assert
        assertThat(resultCmd).isExactlyInstanceOf(FireCmd::class.java)
    }

    @Test
    fun `user cannot give orders to objects that do not belong to him`() {
        // Arrange
        Ioc.resolve<ICommand>(
            dependencyName = "Новый игровой объект",
            args = arrayOf(
                "user_9",
                ObjId("obj_9"),
                mutableMapOf(
                    "id" to "obj_9",
                    "location" to Point(x = 1, y = 1),
                    "velocity" to 1,
                    "angle" to 0.0
                )
            )
        ).execute()

        val testingCmd = InterpreterCmd(
            order = mapOf(
                "gameId" to "game_1",
                "userId" to "user_10",
                "objId" to "obj_9",
                "action" to "Начать движение"
            ) as UniObj
        )

        // Act
        testingCmd.execute()

        // Assert
        Thread.sleep(2000)
        val obj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Получить игровой объект",
            args = arrayOf("user_9", ObjId("obj_9"))
        ).execute()

        val newLocation = obj["location"] as Point
        assertThat(newLocation).isEqualTo(Point(x = 1, y = 1))  // Убеждаемся, что объект не подвинулся
    }

}
