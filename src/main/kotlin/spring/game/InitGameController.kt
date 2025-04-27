package spring.game

import kotlinx.coroutines.runBlocking
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import spring.AuthServiceClient
import spring.UserListDto

@RestController
@RequestMapping("/api")
class InitGameController(
    private val authServiceClient: AuthServiceClient
) {

    @PostMapping("/init-game")
    fun initGame(@RequestBody users: UserListDto): ResponseEntity<String> {
        val gameId = runBlocking {
            authServiceClient.initGame(users)
        }
        return ResponseEntity.ok().body(gameId)
    }
}
