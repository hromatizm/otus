package exception.command

class RetryOnceCommand(
    val command: ICommand,
) : IRetryCommand {

    override fun execute() {
        command.execute()
    }
}