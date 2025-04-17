package spring.cmd

import command.ICommand
import spring.ObjId
import spring.registry.UniObj

class RegisterObjCmd(
    private val objMap: MutableMap<ObjId, UniObj>,
    private val objId: ObjId,
    private val obj: UniObj,
) : ICommand {

    override fun execute() {
        objMap[objId] = obj
    }
}