package spring.message

import com.fasterxml.jackson.databind.ObjectMapper
import command.ICommand
import command.IValueCommand
import ioc.Ioc
import motion.Vector
import org.assertj.core.api.Assertions.assertThat
import org.awaitility.Awaitility
import spring.registry.GameObjRegistry
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import spring.msg.IncomingMessageDto
import spring.msg.ParamDto
import spring.registry.DefaultScopeRegistry
import spring.registry.GameCmdRegistry
import spring.registry.UniObj
import java.time.Duration
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class MsgControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @BeforeEach
    fun setScope() {
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("game_1")
        ).execute()
    }

    @Test
    fun `should return 200 OK`() {
        val request = getRotationRequest(objId = "obj_1", degreesDelta = 0.0)

        mockMvc
            .perform(request)
            .andExpect(status().isOk)
    }

    @Test
    fun `angle should be changed from 0 to 45 after 1 rotation request`() {
        // Arrange
        val objId = "obj_1"
        val request = getRotationRequest(objId = objId, degreesDelta = 45.0)

        val initialObj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()
        val initialVelocity = initialObj.getValue("velocity") as Vector
        val initialAngle = initialVelocity.angle

        // Act
        mockMvc.perform(request)

        // Assert
        assertThat(initialAngle.getDegrees()).isEqualTo(0.0) // начальный угол
        Awaitility.waitAtMost(Duration.ofSeconds(30)).untilAsserted {
            val finalObj = Ioc.resolve<IValueCommand<UniObj>>(
                dependencyName = "Игровой объект",
                args = arrayOf(objId)
            ).execute()
            val finalVelocity = finalObj.getValue("velocity") as Vector
            val finalAngle = finalVelocity.angle
            assertThat(finalAngle.getDegrees()).isEqualTo(45.0) // итоговый угол
        }
    }

    @Test
    fun `angle should be changed from 0 to 90 after 2 rotation requests`() {
        // Arrange
        val objId = "obj_2"
        val request = getRotationRequest(objId = objId, degreesDelta = 45.0)
        val initialObj = Ioc.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()
        val initialVelocity = initialObj.getValue("velocity") as Vector
        val initialAngle = initialVelocity.angle

        // Act
        mockMvc.perform(request)
        mockMvc.perform(request)

        // Assert
        assertThat(initialAngle.getDegrees()).isEqualTo(0.0) // начальный угол
        Awaitility.waitAtMost(Duration.ofSeconds(30)).untilAsserted {
            val finalObj = Ioc.resolve<IValueCommand<UniObj>>(
                dependencyName = "Игровой объект",
                args = arrayOf(objId)
            ).execute()
            val finalVelocity = finalObj.getValue("velocity") as Vector
            val finalAngle = finalVelocity.angle
            assertThat(finalAngle.getDegrees()).isEqualTo(90.0) // итоговый угол
        }
    }

    private fun getRotationRequest(objId: String, degreesDelta: Double): MockHttpServletRequestBuilder {
        val messageDto = IncomingMessageDto(
            params = listOf(
                ParamDto("gameId", "game_1"),
                ParamDto("objId", objId),
                ParamDto("operationId", "Поворот"),
                ParamDto("args", """{"degreesDelta": $degreesDelta}""")
            )
        )
        val messageJson = objectMapper.writeValueAsString(messageDto)
        return post("/api/handle-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }
}
