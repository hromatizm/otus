package exception.logger

import java.util.logging.Logger

class Logger: ILogger {

    companion object {
        val logger: Logger = Logger.getLogger(this::class.java.name)
    }

   override fun log(note: String, exc: Exception){
       logger.info("$note: $exc")
   }

}