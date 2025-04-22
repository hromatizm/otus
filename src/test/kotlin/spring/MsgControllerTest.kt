package spring

import com.fasterxml.jackson.databind.ObjectMapper
import command.IValueCommand
import ioc.Ioc
import motion.Vector
import org.assertj.core.api.Assertions
import org.awaitility.Awaitility
import spring.registry.GameObjRegistry
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spring.msg.IncomingMessageDto
import spring.msg.ParamDto
import spring.registry.UniObj
import java.time.Duration
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("test")
class MsgControllerTest(
    @Value("\${jwt.token.valid}")
    private val validJwtToken: String
) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 200 OK`() {
        val request = getRotationRequest(objId = "obj_1", degreesDelta = 0.0)

        mockMvc
            .perform(request)
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `angle should be changed from 0 to 45 after 1 rotation request`() {
        // Arrange
        val objId = "obj_1"
        val request = getRotationRequest(objId = objId, degreesDelta = 45.0)

        val initialObj = Ioc.Companion.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()
        val initialVelocity = initialObj.getValue("velocity") as Vector
        val initialAngle = initialVelocity.angle

        // Act
        mockMvc.perform(request)

        // Assert
        Assertions.assertThat(initialAngle.getDegrees()).isEqualTo(0.0) // начальный угол
        Awaitility.waitAtMost(Duration.ofSeconds(30)).untilAsserted {
            val finalObj = Ioc.Companion.resolve<IValueCommand<UniObj>>(
                dependencyName = "Игровой объект",
                args = arrayOf(objId)
            ).execute()
            val finalVelocity = finalObj.getValue("velocity") as Vector
            val finalAngle = finalVelocity.angle
            Assertions.assertThat(finalAngle.getDegrees()).isEqualTo(45.0) // итоговый угол
        }
    }

    @Test
    fun `angle should be changed from 0 to 90 after 2 rotation requests`() {
        // Arrange
        val objId = "obj_2"
        val request = getRotationRequest(objId = objId, degreesDelta = 45.0)
        val initialObj = Ioc.Companion.resolve<IValueCommand<UniObj>>(
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()
        val initialVelocity = initialObj.getValue("velocity") as Vector
        val initialAngle = initialVelocity.angle

        // Act
        mockMvc.perform(request)
        mockMvc.perform(request)

        // Assert
        Assertions.assertThat(initialAngle.getDegrees()).isEqualTo(0.0) // начальный угол
        Awaitility.waitAtMost(Duration.ofSeconds(30)).untilAsserted {
            val finalObj = Ioc.Companion.resolve<IValueCommand<UniObj>>(
                dependencyName = "Игровой объект",
                args = arrayOf(objId)
            ).execute()
            val finalVelocity = finalObj.getValue("velocity") as Vector
            val finalAngle = finalVelocity.angle
            Assertions.assertThat(finalAngle.getDegrees()).isEqualTo(90.0) // итоговый угол
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
        return MockMvcRequestBuilders.post("/api/handle-message")
            .contentType(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer $validJwtToken")
            .content(messageJson)
    }
}