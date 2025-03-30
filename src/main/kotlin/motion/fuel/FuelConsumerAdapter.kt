package motion.fuel

import motion.IUniversalObject

class FuelConsumerAdapter(
    val obj: IUniversalObject,
) : IFuelConsumer {

    override fun getFuel(): Double {
        return obj.getProperty("fuel") as Double
    }

    override fun setFuel(newValue: Double) {
        obj.setProperty("fuel" to newValue)
    }
}