package spring.cmd

import command.IValueCommand
import spring.ObjId
import spring.registry.UniObj

class GetObjCmd(
    private val objMap: MutableMap<ObjId, UniObj>,
    private val objId: ObjId,
) : IValueCommand<UniObj> {

    override fun execute(): UniObj {
        return objMap[objId] ?: throw RuntimeException("Объект с id=$objId не найден")
    }
}