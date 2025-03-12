package exception.command

import command.ICommand

class RetryTwiceCommand(
    private val command: ICommand,
) : IRetryCommand {

    override fun execute() {
        command.execute()
    }
}