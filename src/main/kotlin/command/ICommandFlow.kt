package org.example.command

import command.ICommand

interface ICommandFlow {

    fun startFlow()

    fun addCommand(command: ICommand)

    fun stopFlow()
}