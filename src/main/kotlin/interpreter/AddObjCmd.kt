package org.example.interpreter

import command.ICommand
import ioc.Ioc
import spring.ObjId
import spring.registry.UniObj

class AddObjCmd(
    private val userId: String,
    private val objId: ObjId,
    private val obj: UniObj,
) : ICommand {

    override fun execute() {
        // Переключаемся на скоуп игрока
        Ioc.resolve<ICommand>(
            dependencyName = "Scopes.Current",
            args = arrayOf(userId)
        ).execute()
        // Добавляем объект в скоуп игрока
        Ioc.resolve<ICommand>(
            dependencyName = "Ioc.Register",
            args = arrayOf("Игровой объект $objId", { params: Array<out Any> ->
                obj
            })
        ).execute()
    }
}
