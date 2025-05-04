package interpreter

import command.ICommand
import command.IValueCommand
import ioc.Ioc
import spring.ObjId
import spring.registry.UniObj

class GetObjCmd(
    private val userId: String,
    private val objId: ObjId,
) : IValueCommand<UniObj> {

    override fun execute(): UniObj {
        // Переключаемся на скоуп игрока
        Ioc.Companion.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf(userId)
        ).execute()
        // Получаем объект из скоупа игрока
        return Ioc.Companion.resolve<UniObj>(
            dependencyName = "Игровой объект $objId"
        )
    }
}