package spring.msg

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RestController

@RestController
class MsgController(
    private val msgService: IMsgService
) : IMsgController {

    override fun handleMessage(message: IncomingMessageDto): ResponseEntity<Unit> {
        msgService.handleMessage(message)
        return ResponseEntity.ok().build()
    }
}
