package spring.msg

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping

@RequestMapping("/api")
interface IMsgController {

    @PostMapping("/handle-message")
    fun handleMessage(@RequestBody message: IncomingMessageDto): ResponseEntity<Unit>
}
