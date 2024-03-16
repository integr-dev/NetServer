package net.integr


import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.sessions.*
import io.ktor.server.tomcat.*
import io.ktor.util.*
import net.integr.plugins.*
import java.io.File
import kotlin.time.Duration.Companion.seconds

var users = Storage(mutableListOf())
fun main() {
    users = Storage.loadJson()
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)

}

fun Application.module() {
    install(Sessions) {
        val secretSignKey = hex("6819b57a326945c1968f45236589")
        val secretEncryptKey = hex("00112233445566778899aabbccddeeff")
        cookie<UserSession>("user_session", SessionStorageMemory()) {
            cookie.path = "/"
            cookie.maxAgeInSeconds = 60*30 // 30 Minutes
            transform(SessionTransportTransformerEncrypt(secretEncryptKey, secretSignKey))
        }
    }

    install(RateLimit) {
        register(RateLimitName("signup")) {
            rateLimiter(limit = 2, refillPeriod = 60.seconds)
        }
    }

    install(StatusPages) {
        status(HttpStatusCode.TooManyRequests) { call, status ->
            val retryAfter = call.response.headers["Retry-After"]
            call.respondText(text = "429: Too many requests. Wait for $retryAfter seconds.", status = status)
        }

        status(HttpStatusCode.MethodNotAllowed) { call, status ->
            call.respondText(text = "405: Method not allowed", status = status)
        }
    }

    configureRouting()
}

data class UserSession(val username: String)
