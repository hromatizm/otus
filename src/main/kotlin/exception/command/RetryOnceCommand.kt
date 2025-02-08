package exception.command

class RetryOnceCommand(
    private val command: ICommand,
) : IRetryCommand {

    override fun execute() {
        command.execute()
    }
}