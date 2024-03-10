package net.integr


import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.sessions.*
import io.ktor.server.tomcat.*
import io.ktor.util.*
import net.integr.plugins.*
import java.io.File

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

    configureRouting()
}

data class UserSession(val username: String)
