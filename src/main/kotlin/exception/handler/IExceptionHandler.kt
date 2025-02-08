package exception.handler

import exception.command.ICommand

interface IExceptionHandler {

    fun handle(source: ICommand, exc: Exception)
}