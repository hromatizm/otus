package motion.macro

import command.ICommand

class MacroCmd(
    private val commands: List<ICommand>
) : ICommand {

    override fun execute() {
        commands.forEach { it.execute() }
    }
}