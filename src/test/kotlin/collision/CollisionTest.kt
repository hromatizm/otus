package collision

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.Point
import motion.macro.MacroCmd
import org.assertj.core.api.Assertions.assertThat
import org.example.collision.CollisionCheckHandler
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import spring.ObjId
import spring.registry.UniObj

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CollisionTest {

    @BeforeEach
    fun setScope() {
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("game_1")
        ).execute()
    }

    data class Scenario(
        val desc: String,
        val objInitialLocation: Point,
        val objFinalLocation: Point? = null,
        val neighbours: List<Pair<ObjId, Point>>,
        val notNeighbours: List<Pair<ObjId, Point>>,
        val quadrantsWithNeighborsCount: Int,
    )

    val objId = "obj_1"
    val n1 = ObjId("n1")
    val n2 = ObjId("n2")
    val n3 = ObjId("n3")
    val n4 = ObjId("n4")
    val n5 = ObjId("n5")
    val n6 = ObjId("n6")
    val n7 = ObjId("n7")
    val n8 = ObjId("n8")
    val n9 = ObjId("n9")

    fun scenariosForLocatedObj() = listOf(
        Scenario(
            desc = "Объект в 1-й окрестности - нет соседей",
            objInitialLocation = Point(5, 5),
            neighbours = emptyList(),
            notNeighbours = listOf(
                n1 to Point(5, 15),
                n2 to Point(15, 15),
                n3 to Point(15, 5)
            ),
            quadrantsWithNeighborsCount = 0,
        ),
        Scenario(
            desc = "Объект в 1-й окрестности - 1 сосед",
            objInitialLocation = Point(5, 5),
            neighbours = listOf(
                n1 to Point(7, 7)
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 1,
        ),
        Scenario(
            desc = "Объект в 1-й окрестности - 2 соседа",
            objInitialLocation = Point(5, 5),
            neighbours = listOf(
                n1 to Point(7, 7),
                n2 to Point(7, 2),
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 1,
        ),
        Scenario(
            desc = "Объект в 2-х окрестностях - 2 соседа в разных окрестностях",
            objInitialLocation = Point(9, 5),
            neighbours = listOf(
                n1 to Point(2, 2), // левая окрестность
                n2 to Point(15, 2), // правая окрестность
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 2,
        ),
        Scenario(
            desc = "Объект в 2-х окрестностях - 3 соседа",
            objInitialLocation = Point(9, 5),
            neighbours = listOf(
                n1 to Point(x = 2, y = 2), // левая окрестность
                n2 to Point(x = 2, y = 7), // левая окрестность
                n3 to Point(x = 15, y = 2), // правая окрестность
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 2,
        ),
        Scenario(
            desc = "Объект в 4-х окрестностях - 4 соседа в разных окрестностях",
            objInitialLocation = Point(9, 9),
            neighbours = listOf(
                n1 to Point(2, 2), // нижняя левая окрестность
                n2 to Point(15, 2), // нижняя правая окрестность
                n3 to Point(2, 15), // верхняя левая окрестность
                n4 to Point(15, 15), // верхняя правая окрестность
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 4,
        ),
        Scenario(
            desc = "Объект в 4-х окрестностях - 8 соседей в разных окрестностях",
            objInitialLocation = Point(9, 9),
            neighbours = listOf(
                n1 to Point(2, 2), // нижняя левая окрестность
                n2 to Point(2, 7), // нижняя левая окрестность
                n3 to Point(15, 2), // нижняя правая окрестность
                n4 to Point(19, 2), // нижняя правая окрестность
                n5 to Point(2, 15), // верхняя левая окрестность
                n6 to Point(2, 19), // верхняя левая окрестность
                n7 to Point(15, 15), // верхняя правая окрестность
                n8 to Point(19, 19), // верхняя правая окрестность
            ),
            notNeighbours = listOf(n9 to Point(55, 55)),
            quadrantsWithNeighborsCount = 4,
        )
    )

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("scenariosForLocatedObj")
    fun `assert neighbours and commands after objects were located on the field`(scenario: Scenario) {
        // Arrange
        val battleField = BattleField(100, 100).apply {
            addObj(objId = objId, location = scenario.objInitialLocation)           // помещаем на поле объект
            scenario.neighbours.forEach {
                addObj(objId = it.first.id, location = it.second)                   // помещаем на поле соседей
            }
            scenario.notNeighbours.forEach {
                addObj(objId = it.first.id, location = it.second)                   // помещаем на поле НЕ соседей
            }
        }
        val quadrants = CollisionQuadrants(battleField = battleField, shift = 0)    // окрестности без смещения
        quadrants.setPosition(ObjId(objId))                                         // размещаем объект в окрестности
        val testingChain = CollisionCheckHandler(collisionQuadrants = quadrants)

        // Act
        testingChain.handle(objId)

        // Assert
        assertThatNeighboursWereDetectedCorrectly(quadrants, scenario)
        assertThatCorrectCommandsWereCreated(quadrants, scenario)
    }

    fun scenariosForChangingLocationObj() = listOf(
        Scenario(
            desc = "Перемещение в той же окрестности - был 1 сосед, стало 1",
            objInitialLocation = Point(8, 8),
            objFinalLocation = Point(7, 7),
            neighbours = listOf(
                n1 to Point(5, 5),
            ),
            notNeighbours = listOf(
                n2 to Point(5, 15),
                n3 to Point(15, 5)
            ),
            quadrantsWithNeighborsCount = 1,
        ),
        Scenario(
            desc = "Перемещение в другую окрестность по диагонали - был 1 сосед, стало 0",
            objInitialLocation = Point(8, 8),
            objFinalLocation = Point(12, 12),
            neighbours = emptyList(),
            notNeighbours = listOf(
                n1 to Point(5, 5), // изначальный сосед
                n2 to Point(5, 15),
                n3 to Point(15, 5)
            ),
            quadrantsWithNeighborsCount = 0,
        ),
        Scenario(
            desc = "Перемещение из одной окрестности в две - был 1 сосед, стало 2",
            objInitialLocation = Point(8, 8),
            objFinalLocation = Point(10, 8),
            neighbours = listOf(
                n1 to Point(5, 5), // левый
                n2 to Point(20, 5), // правый
            ),
            notNeighbours = listOf(
                n3 to Point(5, 15)
            ),
            quadrantsWithNeighborsCount = 2,
        ),
        Scenario(
            desc = "Перемещение из одной окрестности в 4 - был 1 сосед, стало 4",
            objInitialLocation = Point(8, 8),
            objFinalLocation = Point(10, 10),
            neighbours = listOf(
                n1 to Point(5, 5),  // левый нижний
                n2 to Point(5, 20), // левый верхний
                n3 to Point(15, 5), // правый нижний
                n4 to Point(15, 15), // правый верхний
            ),
            notNeighbours = listOf(
                n5 to Point(30, 5)
            ),
            quadrantsWithNeighborsCount = 4,
        ),
    )

    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("scenariosForChangingLocationObj")
    fun `assert neighbours and commands after object change location`(scenario: Scenario) {
        // Arrange
        val battleField = BattleField(100, 100).apply {
            val obj = addObj(objId = objId, location = scenario.objInitialLocation)     // помещаем на поле объект
            scenario.neighbours.forEach {
                addObj(objId = it.first.id, location = it.second)                       // помещаем на поле соседей
            }
            scenario.notNeighbours.forEach {
                addObj(objId = it.first.id, location = it.second)                       // помещаем на поле НЕ соседей
            }
            obj["location"] = scenario.objFinalLocation!!
            setPosition(objId)                                                          // перемещаем объект по полю
        }
        val quadrants = CollisionQuadrants(battleField = battleField, shift = 0)        // окрестности без смещения
        quadrants.setPosition(ObjId(objId))                                             // размещаем объект в окрестности
        val testingChain = CollisionCheckHandler(collisionQuadrants = quadrants)

        // Act
        testingChain.handle(objId)

        // Assert
        assertThatNeighboursWereDetectedCorrectly(quadrants, scenario)
        assertThatCorrectCommandsWereCreated(quadrants, scenario)
    }

    private fun assertThatNeighboursWereDetectedCorrectly(
        quadrants: CollisionQuadrants,
        scenario: Scenario
    ) {
        // Убеждаемся, что корректно определены окрестности, в которых находятся соседи проверяемого объекта
        val neighbourQuadrants: List<Pair<Quadrant, Set<ObjId>>> = quadrants.findNeighbors(ObjId(objId))
        assertThat(neighbourQuadrants).hasSize(scenario.quadrantsWithNeighborsCount)

        // Убеждаемся, что корректно определны соседи проверяемого объекта
        val calculatedNeighbours = neighbourQuadrants.map { it.second }.flatten()
        val targetNeighbours = scenario.neighbours.map { it.first }
        assertThat(calculatedNeighbours).containsExactlyInAnyOrderElementsOf(targetNeighbours)
    }

    private fun assertThatCorrectCommandsWereCreated(
        quadrants: CollisionQuadrants,
        scenario: Scenario
    ) {
        val neighbourQuadrants: List<Pair<Quadrant, Set<ObjId>>> = quadrants.findNeighbors(ObjId(objId))
        // Убеждаемся, что для каждой окрестности создана макро-команда
        val checkCollisionMacroCommands = neighbourQuadrants.flatMap { it.first.checkCollisionCommands.values }
        assertThat(checkCollisionMacroCommands)
            .allSatisfy { cmd ->
                assertThat(cmd).isExactlyInstanceOf(MacroCmd::class.java)
            }
        // Убеждаемся, что внутри макро-команды находятся команды проверки коллизии между 2-мя объектами
        val collisionCheckerCommands = checkCollisionMacroCommands.map { it as MacroCmd }.flatMap { it.commands }
        assertThat(collisionCheckerCommands)
            .allSatisfy { cmd ->
                assertThat(cmd).isExactlyInstanceOf(CollisionCheckerCmd::class.java)
            }
        // Убеждаемся, что комманды проверяют корректные объекты
        val commandsParams =
            collisionCheckerCommands
                .map { it as CollisionCheckerCmd }
                .map { it.obj to it.neighborId }
        // первый объект в команде - объект-инициатор проверки
        assertThat(commandsParams).allSatisfy { it.first["id"] == objId }
        // второй объект в команде - корректный сосед
        val neighboursFromCommands = commandsParams.map { it.second }
        val targetNeighbours = scenario.neighbours.map { it.first.id }
        assertThat(neighboursFromCommands).containsExactlyInAnyOrderElementsOf(targetNeighbours)
    }

    private fun BattleField.addObj(objId: String, location: Point): UniObj {
        addGameObj(objId, location)
        val obj = getGameObj(objId)
        this.setPosition(objId)
        return obj
    }

    private fun addGameObj(objId: String, location: Point) {
        val cmd = Ioc.resolve<ICommand>(
            dependencyName = "Добавить игровой объект",
            args = arrayOf(
                objId,
                mutableMapOf(
                    "id" to objId,
                    "size" to 3,
                    "location" to location
                )
            )
        )
        cmd.execute()
    }

    private fun getGameObj(objId: String): UniObj {
        val cmd = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        )
        return cmd.execute()
    }
}
