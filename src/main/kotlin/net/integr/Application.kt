package net.integr

import io.ktor.client.*
import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.sessions.*
import io.ktor.server.tomcat.*
import io.ktor.util.*
import io.ktor.websocket.*
import net.integr.plugins.*

fun main() {
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

    configureSecurity()
    configureRouting()
}

data class UserSession(val username: String)
