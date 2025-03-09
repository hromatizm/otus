package exception.handler

import command.ICommand

interface IExceptionHandler {

    fun handle(source: ICommand, exc: Exception)
}