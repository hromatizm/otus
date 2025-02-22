package org.example.motion.fuel

import exception.command.ICommand
import org.example.exception.CommandException

class CheckFuelCmd(
    private val obj: IFuelConsumer,
    private val minValue: Double,
) : ICommand {

    override fun execute() {
        if (obj.getFuel() < minValue) throw CommandException("Not enough fuel")
    }
}