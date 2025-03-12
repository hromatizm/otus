package motion.move

import command.ICommand

class SetLocationCmd(
    private val obj: MutableMap<String, Any>,
    private val newValue: Any
) : ICommand {

    override fun execute() {
        obj["location"] = newValue
    }
}