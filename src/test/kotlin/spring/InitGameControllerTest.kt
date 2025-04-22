package spring

import com.fasterxml.jackson.databind.ObjectMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.coVerify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@SpringBootTest
@AutoConfigureMockMvc
class InitGameControllerTest() {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    private val uri = "/api/init-game"
    private val gameId = "game-123"
    private val userList = UserListDto(
        users = listOf(
            UserDto("user_id_1", "user_nic_1"),
            UserDto("user_id_2", "user_nic_2"),
        )
    )

    @MockkBean
    private lateinit var authServiceClient: AuthServiceClient

    @Test
    fun `the answer is OK`() {
        // Arrange
        coEvery { authServiceClient.initGame(userList) } returns gameId
        val request = createInitGameRequest(userList)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        resultActions.andExpect(status().isOk)
    }

    @Test
    fun `should invoke authServiceClient`() {
        // Arrange
        coEvery { authServiceClient.initGame(userList) } returns gameId
        val request = createInitGameRequest(userList)

        // Act
        val resultActions: ResultActions = mockMvc.perform(request)

        // Assert
        coVerify { authServiceClient.initGame(userList) }
    }

    private fun createInitGameRequest(userList: UserListDto): MockHttpServletRequestBuilder {
        val messageJson = objectMapper.writeValueAsString(userList)
        return post(uri)
            .contentType(MediaType.APPLICATION_JSON)
            .content(messageJson)
    }

}