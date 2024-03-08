package net.integr.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import kotlinx.css.*
import kotlinx.html.*
import java.io.File

fun Application.configureRouting() {
    routing {
        staticResources("/", "static") {
            default("logo.jpg")
        }
        get("/") {
            call.respondHtml {
                head {
                    title {
                        +"Login Panel"
                    }
                    link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                    link(rel="stylesheet", href="/styles.css", type = "text/css")
                }
                body {
                    form(action = "/login", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                        div(classes = "login") {
                            img(classes = "img", src = "/static/logo.jpg")
                            p(classes = "pad") {
                                +"Username"
                                textInput(name = "username", classes = "form-control")
                            }
                            p(classes = "pad") {
                                +"Password"
                                passwordInput(name = "password", classes = "form-control")
                            }
                            p(classes = "pad") {
                                submitInput(classes = "btn btn-success") { value = "Login" }
                            }
                        }
                    }
                }
            }
        }

        post("/login") {
            val formParameters = call.receiveParameters()
            val username = formParameters["username"].toString()
            val password = formParameters["password"].toString()

            call.respondText("Username: $username \nPassword: $password")
        }

        get("/styles.css") {
            call.respondCss {
                rule(".pad") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                }

                rule(".login") {
                    margin = Margin(LinearDimension("20%"))
                }

                rule("html") {
                    height = LinearDimension("100%")
                    width = LinearDimension("100%")
                }

                rule(".img") {
                    margin = Margin(LinearDimension("auto"))
                    borderRadius = LinearDimension("100%")
                    height = LinearDimension("110px")
                    width = LinearDimension("110px")
                    display = Display.block
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}
