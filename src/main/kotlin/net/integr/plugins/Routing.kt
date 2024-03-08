package net.integr.plugins

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.sessions.*
import kotlinx.css.*
import kotlinx.html.*
import kotlinx.serialization.builtins.serializer
import net.integr.UserSession
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

fun Application.configureRouting() {
    routing {
        staticResources("/", "static")

        get("/") {
            call.respondRedirect("/login")
        }

        get("/login") {
            val userSession = call.sessions.get<UserSession>()
            if (userSession == null) {
                call.respondHtml {
                    head {
                        title {
                            +"Login Panel"
                        }
                        link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                        script {
                            src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                        }
                        link(rel="stylesheet", href="/styles.css", type = "text/css")
                        link(rel="icon", type="image/png", href="/logo_round.png")
                    }
                    body {
                        if (call.parameters.contains("logging_out")) {
                            div(classes = "px") {
                                div(classes = "alert alert-success alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Successfully logged out!"
                                    a(href = "/login", classes = "inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        if (call.parameters.contains("retry_login")) {
                            div(classes = "px") {
                                div(classes = "alert alert-warning alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Invalid credentials!"
                                    a(href = "/login", classes = "inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        if (call.parameters.contains("not_logged_in")) {
                            div(classes = "px") {
                                div(classes = "alert alert-warning alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Please sign in first!"
                                    a(href = "/login") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        form(action = "/login_internal", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                            div(classes = "login") {
                                img(classes = "img", src = "/logo.jpg")
                                p(classes = "title") {
                                    +"NetServer"
                                }
                                p(classes = "pad") {
                                    +"Username"
                                    textInput(name = "username", classes = "form-control") {
                                        required = true
                                    }
                                }
                                p(classes = "pad") {
                                    +"Password"
                                    passwordInput(name = "password", classes = "form-control") {
                                        required = true
                                    }
                                }
                                p(classes = "pad") {
                                    button(classes = "btn btn-success", type = ButtonType.submit) {
                                        i(classes = "fa-solid fa-right-to-bracket")
                                        +" Login"
                                    }

                                    a(href = "/signup", classes = "pxh") {
                                        button(classes = "btn btn-warning", type = ButtonType.button) {
                                            i(classes = "fa-solid fa-user-plus")
                                            +" Signup"
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                call.respondRedirect("/account")
            }
        }

        get("/signup") {
            call.respondHtml {
                head {
                    title {
                        +"Login Panel"
                    }
                    link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    link(rel="stylesheet", href="/styles.css", type = "text/css")
                    link(rel="icon", type="image/png", href="/logo_round.png")
                }
                body {
                    if (call.parameters.contains("retry_signup")) {
                        val errorRegion = call.parameters["retry_signup"]
                        div(classes = "px") {
                            div(classes = "alert alert-warning alert-dismissible fade show") {
                                i(classes = "fa-solid fa-triangle-exclamation")
                                +" $errorRegion not accepted!"
                                a(href = "/signup", classes = "inherit") {
                                    button(type = ButtonType.button, classes = "close") {
                                        i(classes = "fa-solid fa-xmark")
                                    }
                                }
                            }
                        }
                    }

                    form(classes = "form-inline") {
                        a(href = "/login", classes = "px") {
                            button(classes = "btn btn-warning", type = ButtonType.button, formMethod = ButtonFormMethod.post) {
                                i(classes = "fa-solid fa-reply")
                                +" Back"
                            }
                        }
                    }

                    form(action = "/signup_internal", encType = FormEncType.applicationXWwwFormUrlEncoded, method = FormMethod.post) {
                        div(classes = "login") {
                            img(classes = "img", src = "/logo.jpg")
                            p(classes = "title") {
                                +"NetServer Signup"
                            }
                            p(classes = "pad") {
                                +"Username"
                                textInput(name = "username", classes = "form-control") {
                                    required = true
                                }
                            }
                            p(classes = "pad") {
                                +"Email"
                                emailInput(name = "email", classes = "form-control") {
                                    required = true
                                }
                            }
                            p(classes = "pad") {
                                +"Password"
                                passwordInput(name = "password", classes = "form-control") {
                                    required = true
                                }
                            }
                            p(classes = "pad") {
                                checkBoxInput(name = "agree_tos") {
                                    required = true
                                }

                                +" I agree to the Terms of Service"
                            }
                            p(classes = "pad") {
                                button(classes = "btn btn-warning", type = ButtonType.submit) {
                                    i(classes = "fa-solid fa-user-plus")
                                    +" Signup"
                                }
                            }
                        }
                    }
                }
            }
        }

        get("/account") {
            if (call.sessions.get<UserSession>() == null) call.respondRedirect("/login?not_logged_in")

            call.respondHtml {
                head {
                    title {
                        +"Account"
                    }
                    link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                    link(rel="stylesheet", href="/account_styles.css", type = "text/css")
                    link(rel="icon", type="image/png", href="/logo_round.png")
                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                }
                body {
                    form(classes = "form-inline") {
                        a(href = "/logout_internal", classes = "pad") {
                            button(classes = "btn btn-warning", type = ButtonType.button) {
                                i(classes = "fa-solid fa-right-from-bracket")
                                +" Logout"
                            }
                        }
                    }

                    div(classes = "center") {
                        p(classes = "title") {
                            +"Welcome ${call.sessions.get<UserSession>()!!.username}"
                        }
                    }
                }
            }
        }

        post("/login_internal") {
            val formParameters = call.receiveParameters()
            val username = formParameters["username"].toString()
            val password = formParameters["password"].toString()

            if (hash(password) == hash("Integr") && username == "Integr") {
                call.sessions.set(UserSession(username = username))
                call.respondRedirect("/account")
            } else call.respondRedirect("/login?retry_login")
        }

        post("/signup_internal") {
            val formParameters = call.receiveParameters()
            val username = formParameters["username"].toString()
            val password = formParameters["password"].toString()
            val email = formParameters["email"].toString()

            if (username != "Integr") call.respondRedirect("/signup?retry_signup=Username")
            if (email != "integr@integr.is-a.dev") call.respondRedirect("/signup?retry_signup=Email")
            if (hash(password) != hash("Integr")) call.respondRedirect("/signup?retry_signup=Password")

            call.sessions.set(UserSession(username = username))
            call.respondRedirect("/account")
        }

        get("/logout_internal") {
            if (call.sessions.get<UserSession>() != null) {
                call.sessions.clear<UserSession>()
            }

            call.respondRedirect("/login?logging_out")
        }

        get("/styles.css") {
            call.respondCss {
                rule(".pad") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                }

                rule(".px") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("10px"))
                }

                rule(".pxh") {
                    margin = Margin(LinearDimension("0px"), LinearDimension("10px"))
                }

                rule(".inherit") {
                    color = Color.inherit
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

                rule(".title") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                    fontSize = LinearDimension("50px")
                    textAlign = TextAlign.center
                    fontWeight = FontWeight.bolder
                }
            }
        }

        get("/account_styles.css") {
            call.respondCss {
                rule(".pad") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("10px"))
                    display = Display.block
                }

                rule(".center") {
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

                rule(".title") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                    fontSize = LinearDimension("50px")
                    textAlign = TextAlign.center
                    fontWeight = FontWeight.bolder
                }
            }
        }
    }
}

suspend inline fun ApplicationCall.respondCss(builder: CssBuilder.() -> Unit) {
    this.respondText(CssBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun hash(password: String): String {
    return BigInteger(1, MessageDigest.getInstance("SHA-512").digest(password.toByteArray())).toString(16)
}
