package spring.msg

data class IncomingMessageDto(
    val params: List<ParamDto>,
)

data class ParamDto(
    val code: String,
    val value: String,
)