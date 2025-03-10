package command

class FinishCmd(
    private val obj: MutableMap<String, Any>
) : ICommand {

    override fun execute() {
        obj["finished"] = true
    }
}
