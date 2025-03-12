package org.example.motion.fuel

import command.ICommand

class BurnFuelCmd(
    private val obj: IFuelConsumer,
    private val value: Double,
) : ICommand {

    override fun execute() {
        val currentValue = obj.getFuel()
        obj.setFuel(currentValue - value)
    }
}