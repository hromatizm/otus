package spring

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.springframework.stereotype.Component

@Component
class AuthServiceClient(
    private val httpClient: HttpClient
) {

    private val baseUrl = "http://localhost:8081/api"

    suspend fun getPublicKey(): String {
        val response = httpClient.get("$baseUrl/public-key")
        return response.bodyAsText()
    }

    suspend fun initGame(users: UserListDto): String {
        val response: HttpResponse = httpClient.post("$baseUrl/init-game") {
            contentType(ContentType.Application.Json)
            setBody(users)
        }
        return response.bodyAsText()
    }
}
