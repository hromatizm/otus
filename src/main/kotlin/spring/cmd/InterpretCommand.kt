package spring.cmd

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import command.ICommand
import command.IValueCommand
import ioc.Ioc
import spring.registry.UniObj

class InterpretCommand(
    private val gameOrderParams: Map<String, String>
) : ICommand {

    override fun execute() {
        val gameId = gameOrderParams["gameId"] as String
        val objId = gameOrderParams["objId"] as String
        val operationId = gameOrderParams["operationId"] as String
        val argsJson = gameOrderParams["args"] as String

        Ioc.resolve<ICommand>( // переходим в скоуп игры
            dependencyName = "Scopes.Current",
            args = arrayOf(gameId)
        ).execute()

        val obj = Ioc.resolve<IValueCommand<UniObj>>( // получаем игровой объект
            dependencyName = "Игровой объект",
            args = arrayOf(objId)
        ).execute()

        val commandArgs = toMap(argsJson) // парсим аргументы комадны в мапу

        val command = Ioc.resolve<ICommand>(
            dependencyName = operationId,
            args = arrayOf(obj, commandArgs)
        )

        Ioc.resolve<ICommand>( // добавляем команду в евент луп игры
            dependencyName = "Добавить команду в игровой цикл",
            args = arrayOf(gameId, command)
        ).execute()
    }

    companion object {
        private val jsonMapper = jacksonObjectMapper()

        private fun toMap(argsJson: String): Map<String, Any> {
            return jsonMapper.readValue(
                argsJson.toByteArray(),
                object : TypeReference<Map<String, Any>>() {}
            )
        }
    }
}