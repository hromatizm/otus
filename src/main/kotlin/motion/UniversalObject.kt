package motion

class UniversalObject : IUniversalObject {

    private val properties = mutableMapOf<String, Any?>()

    override fun getProperty(name: String): Any? {
        return properties[name]
    }

    override fun setProperty(nameToValue: Pair<String, Any>) {
        properties[nameToValue.first] = nameToValue.second
    }
}