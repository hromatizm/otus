package exception.command

interface ICommandFlow {

    fun startFlow()

    fun addCommand(command: ICommand)

    fun stopFlow()
}