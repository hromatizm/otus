package spring.config

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.jackson.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KtorConfig {

    @Bean
    fun ktorClient(): HttpClient {
        return HttpClient(CIO) {
            install(ContentNegotiation) {
                jackson()
            }
        }
    }
}
