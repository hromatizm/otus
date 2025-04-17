package spring.msg

fun IncomingMessageDto.toModel() =
    IncomingMessageModel(
        params = params.map { it.toModel() },
    )

fun ParamDto.toModel() =
    ParamModel(
        code = code,
        value = value,
    )
