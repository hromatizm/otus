package spring.msg

interface IMsgService {

    fun handleMessage(message: IncomingMessageDto)
}
