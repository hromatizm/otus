package exception.command

import command.ICommand

class RetryOnceCommand(
    val command: ICommand,
) : IRetryCommand {

    override fun execute() {
        command.execute()
    }
}