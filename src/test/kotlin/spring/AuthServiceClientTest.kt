package spring

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class AuthServiceClientTest {

    private val gameId = "game-123"
    private val userList = UserListDto(
        users = listOf(
            UserDto("user_id_1", "user_nic_1"),
            UserDto("user_id_2", "user_nic_2"),
        )
    )

    @Test
    fun `initGame should return gameId`() = runTest {
        // Arrange
        val mockEngine = MockEngine { request ->
            respond(
                content = gameId,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "text/plain")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(
                    kotlinx.serialization.json.Json {
                        prettyPrint = true
                    }
                )
            }
        }
        val authServiceClient = AuthServiceClient(client)

        // Act
        val receivedGameId = authServiceClient.initGame(userList)

        // Assert
        assertThat(receivedGameId).isEqualTo(gameId)
    }

}
