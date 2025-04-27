package spring

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import spring.msg.IncomingMessageDto
import spring.msg.ParamDto
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class JwtAuthTest(
    @Value("\${jwt.token.valid}")
    private val validJwtToken: String,
    @Value("\${jwt.token.expired}")
    private val expiredJwtToken: String
) {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Test
    fun `should return 403-forbidden without token`() {
        // Arrange
        val request = getRotationRequest()

        // Act
        val resultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `should return 403-forbidden with expired token`() {
        // Arrange
        val request = getRotationRequest()
        request.header(HttpHeaders.AUTHORIZATION, "Bearer $expiredJwtToken")

        // Act
        val resultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isForbidden)
    }

    @Test
    fun `should return 200 OK with valid token`() {
        // Arrange
        val request = getRotationRequest()
        request.header(HttpHeaders.AUTHORIZATION, "Bearer $validJwtToken")

        // Act
        val resultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
    }

    private fun getRotationRequest(): MockHttpServletRequestBuilder {
        val messageDto = IncomingMessageDto(
            params = listOf(
                ParamDto("gameId", "game_1"),
                ParamDto("objId", "obj_1"),
                ParamDto("operationId", "Поворот"),
                ParamDto("args", """{"degreesDelta": "45.0"}""")
            )
        )
        val messageJson = objectMapper.writeValueAsString(messageDto)
        return MockMvcRequestBuilders.post("/api/handle-message")
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }
}