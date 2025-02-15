package exception.command

class RetryTwiceCommand(
    private val command: ICommand,
) : IRetryCommand {

    override fun execute() {
        command.execute()
    }
}