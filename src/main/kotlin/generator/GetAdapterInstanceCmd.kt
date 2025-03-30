package generator

import command.IValueCommand
import ioc.Ioc

class GetAdapterInstanceCmd(
    private val interfaceType: Class<*>,
    private val obj: MutableMap<String, Any>
) : IValueCommand<Any> {

    override fun execute(): Any {
        val adapterName = "Generated${interfaceType.simpleName.drop(1)}Adapter"
        val clazz = Ioc.resolve<IValueCommand<Class<*>>>(
            dependencyName = "LoadClassFromFile",
            args = arrayOf("build", adapterName)
        ).execute()
        val instance = clazz.getDeclaredConstructor(MutableMap::class.java).newInstance(obj)
        return instance
    }
}
