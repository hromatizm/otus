package org.example

import command.ICommand
import command.CommandFlow


suspend fun main() {
    val aComm = PrintACommand()
    val bComm = PrintBCommand()
    val commandFlow = CommandFlow()
    with(commandFlow) {
        startFlow()
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)
        addCommand(aComm)
        addCommand(bComm)

    }
    Thread.sleep(1_000)
}

class PrintACommand : ICommand {
    override fun execute() {
        println("A")
    }
}

class PrintBCommand : ICommand {
    override fun execute() {
        println("B")
    }
}
