package spring.msg

import command.ICommand
import ioc.Ioc
import org.springframework.stereotype.Service

@Service
class MsgService : IMsgService {

    override fun handleMessage(message: IncomingMessageDto) {
        val paramsMap = message.params.associate { it.code to it.value }

        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf("default")
        ).execute()

        Ioc.resolve<ICommand>(
            dependencyName = "Интерпретатор команд",
            args = arrayOf(paramsMap)
        ).execute()
    }
}