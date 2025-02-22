package org.example.motion.macro

import exception.command.ICommand

class MacroCmd(
    private val commands: List<ICommand>
) : ICommand {

    override fun execute() {
        commands.forEach { it.execute() }
    }
}