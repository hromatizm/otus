package exception.logger

interface ILogger {

    fun log(note: String, exc: Exception)
}