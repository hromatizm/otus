package motion.fuel

import command.ICommand
import exception.CommandException

class CheckFuelCmd(
    private val obj: IFuelConsumer,
    private val minValue: Double,
) : ICommand {

    override fun execute() {
        if (obj.getFuel() < minValue) throw CommandException("Not enough fuel")
    }
}