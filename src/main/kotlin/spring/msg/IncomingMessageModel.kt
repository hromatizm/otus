package spring.msg


data class IncomingMessageModel(
    val params: List<ParamModel>,
)

data class ParamModel(
    val code: String,
    val value: String,
)