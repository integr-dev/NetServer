package net.integr.plugins

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.sessions.*
import io.ktor.util.Identity.decode
import kotlinx.css.*
import kotlinx.html.*
import net.integr.UserSession
import net.integr.users
import org.jetbrains.annotations.Debug
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
                    lang = "en"
                    head {
                        title {
                            +"Login"
                        }

                        link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                        link(rel="stylesheet", href="/styles.css", type = "text/css")
                        link(rel="icon", type="image/png", href="/logo_round.png")

                        script {
                            src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                        }
                        script {
                            src = "https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"
                        }
                        script {
                            +"function _0x419e(){var _0x1d34e6=['value','209442UhFvkF','57771fPhOdv','8SAoktx','MD5','2831880JrlpSo','40eDBBRz','password','2119491xEblHV','715020innXbO','136017sqCKPz','2ShsMFw','trim','getElementById','289086RCNdba'];_0x419e=function(){return _0x1d34e6;};return _0x419e();}function _0xa9ae(_0x505a27,_0x2871d7){var _0x419ef7=_0x419e();return _0xa9ae=function(_0xa9aea9,_0x8fc095){_0xa9aea9=_0xa9aea9-0x13b;var _0x1be774=_0x419ef7[_0xa9aea9];return _0x1be774;},_0xa9ae(_0x505a27,_0x2871d7);}(function(_0x5c2e74,_0x4c2d92){var _0x1765aa=_0xa9ae,_0x378889=_0x5c2e74();while(!![]){try{var _0x59a507=parseInt(_0x1765aa(0x147))/0x1*(-parseInt(_0x1765aa(0x13b))/0x2)+parseInt(_0x1765aa(0x13e))/0x3*(parseInt(_0x1765aa(0x142))/0x4)+parseInt(_0x1765aa(0x145))/0x5+parseInt(_0x1765aa(0x13d))/0x6+parseInt(_0x1765aa(0x146))/0x7+-parseInt(_0x1765aa(0x13f))/0x8*(parseInt(_0x1765aa(0x144))/0x9)+parseInt(_0x1765aa(0x141))/0xa;if(_0x59a507===_0x4c2d92)break;else _0x378889['push'](_0x378889['shift']());}catch(_0x38671b){_0x378889['push'](_0x378889['shift']());}}}(_0x419e,0x24423));function encrypt(){var _0x561913=_0xa9ae,_0x229483=document['getElementById'](_0x561913(0x143))[_0x561913(0x13c)];if(_0x229483[_0x561913(0x148)]()==='')return;var _0x560ac7=CryptoJS[_0x561913(0x140)](_0x229483);document[_0x561913(0x149)](_0x561913(0x143))[_0x561913(0x13c)]=_0x560ac7;}"
                        }

                        meta("description", content = "NetServer Test")
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
                                    +" Username and/or Password incorrect!"
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

                        if (call.parameters.contains("deleted")) {
                            div(classes = "px") {
                                div(classes = "alert alert-success alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Successfully deleted your account!"
                                    a(href = "/login") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        form(action = "/login_internal", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                            div(classes = "login") {
                                img(classes = "img", src = "/logo.jpg", alt = "Logo")
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
                                        id = "password"
                                    }
                                }
                                p(classes = "pad") {
                                    button(classes = "btn btn-success", type = ButtonType.submit) {
                                        i(classes = "fa-solid fa-right-to-bracket")
                                        +" Login"
                                    }

                                    a(href = "/signup", classes = "pxh") {
                                        button(classes = "btn btn-warning", type = ButtonType.button) {
                                            onClick = "_0x419e()"
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
                lang = "en"
                head {
                    title {
                        +"Signup"
                    }
                    link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                    link(rel="stylesheet", href="/styles.css", type = "text/css")
                    link(rel="icon", type="image/png", href="/logo_round.png")

                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    script {
                        src = "https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"
                    }
                    script {
                        +"function _0x419e(){var _0x1d34e6=['value','209442UhFvkF','57771fPhOdv','8SAoktx','MD5','2831880JrlpSo','40eDBBRz','password','2119491xEblHV','715020innXbO','136017sqCKPz','2ShsMFw','trim','getElementById','289086RCNdba'];_0x419e=function(){return _0x1d34e6;};return _0x419e();}function _0xa9ae(_0x505a27,_0x2871d7){var _0x419ef7=_0x419e();return _0xa9ae=function(_0xa9aea9,_0x8fc095){_0xa9aea9=_0xa9aea9-0x13b;var _0x1be774=_0x419ef7[_0xa9aea9];return _0x1be774;},_0xa9ae(_0x505a27,_0x2871d7);}(function(_0x5c2e74,_0x4c2d92){var _0x1765aa=_0xa9ae,_0x378889=_0x5c2e74();while(!![]){try{var _0x59a507=parseInt(_0x1765aa(0x147))/0x1*(-parseInt(_0x1765aa(0x13b))/0x2)+parseInt(_0x1765aa(0x13e))/0x3*(parseInt(_0x1765aa(0x142))/0x4)+parseInt(_0x1765aa(0x145))/0x5+parseInt(_0x1765aa(0x13d))/0x6+parseInt(_0x1765aa(0x146))/0x7+-parseInt(_0x1765aa(0x13f))/0x8*(parseInt(_0x1765aa(0x144))/0x9)+parseInt(_0x1765aa(0x141))/0xa;if(_0x59a507===_0x4c2d92)break;else _0x378889['push'](_0x378889['shift']());}catch(_0x38671b){_0x378889['push'](_0x378889['shift']());}}}(_0x419e,0x24423));function encrypt(){var _0x561913=_0xa9ae,_0x229483=document['getElementById'](_0x561913(0x143))[_0x561913(0x13c)];if(_0x229483[_0x561913(0x148)]()==='')return;var _0x560ac7=CryptoJS[_0x561913(0x140)](_0x229483);document[_0x561913(0x149)](_0x561913(0x143))[_0x561913(0x13c)]=_0x560ac7;}"
                    }

                    meta("description", content = "NetServer Test")
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

                    form(action = "/signup_internal", encType = FormEncType.multipartFormData, method = FormMethod.post) {
                        div(classes = "login") {
                            img(classes = "img", src = "/logo.jpg", alt = "Logo")

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
                                    id = "password"
                                }
                            }

                            p(classes = "pad") {
                                checkBoxInput(name = "agree_tos") {
                                    required = true
                                }

                                +" I agree to the "
                                a(href = "/tos") {+"Terms of Service"}
                            }

                            p(classes = "pad") {
                                button(classes = "btn btn-warning", type = ButtonType.submit) {
                                    onClick = "_0x419e()"
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
                lang = "en"
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
                    meta("description", content = "NetServer Test")
                }
                body {
                    form(classes = "form-inline") {
                        a(href = "/logout_internal", classes = "pad") {
                            button(classes = "btn btn-warning", type = ButtonType.button) {
                                i(classes = "fa-solid fa-right-from-bracket")
                                +" Logout"
                            }
                        }

                        a(href = "/delete_internal", classes = "pad") {
                            button(classes = "btn btn-danger", type = ButtonType.button) {
                                i(classes = "fa-solid fa-trash")
                                +" Delete account"
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

        get("/delete_internal") {
            if (call.sessions.get<UserSession>() != null) {
                val userSession = call.sessions.get<UserSession>()
                val username = userSession!!.username

                users.users.remove(users.getFromName(username))
                call.sessions.clear<UserSession>()
                users.saveJson()

                call.respondRedirect("/login?deleted")
            } else call.respondRedirect("/login?not_logged_in")
        }

        post("/login_internal") {
            val data = call.receiveMultipart()
            var username = ""
            var password = ""

            data.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "username") username = part.value
                    if (part.name == "password") password = part.value
                }
            }

            if (users.getFromName(username) != null && users.getFromName(username)!!.passwordHash == hash(password)) {
                call.sessions.set(UserSession(username = username))
                call.respondRedirect("/account")
            } else call.respondRedirect("/login?retry_login")
        }

        post("/signup_internal") {
            val data = call.receiveMultipart()
            var username = ""
            var password = ""
            var email = ""

            data.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "username") username = part.value
                    if (part.name == "password") password = part.value
                    if (part.name == "email") email = part.value
                }
            }

            if (users.getFromName(username) == null) {
                if (users.getFromEmail(email) == null) {
                    val user = UserObj(username, hash(password), email)
                    users.users += user
                    users.saveJson()

                    call.sessions.set(UserSession(username = username))
                    call.respondRedirect("/account")
                } else call.respondRedirect("/signup?retry_signup=Email")
            } else call.respondRedirect("/signup?retry_signup=Username")
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
                    margin = Margin(LinearDimension("5%"), LinearDimension("20%"))
                }

                rule("html") {
                    height = LinearDimension("100%")
                    width = LinearDimension("100%")
                }

                rule(".center") {
                    margin = Margin(LinearDimension("20%"))
                    textAlign = TextAlign.start
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

        get("/tos") {
            call.respondHtml {
                head {
                    title {
                        +"Terms of Service"
                    }
                    link(rel="stylesheet", href="https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css", type = "text/css")
                    link(rel="stylesheet", href="/styles.css", type = "text/css")
                    link(rel="icon", type="image/png", href="/logo_round.png")
                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    meta("viewport", content="width=device-width, initial-scale=1")
                }
                body {
                    form(classes = "form-inline") {
                        a(href = "/signup", classes = "px") {
                            button(classes = "btn btn-warning", type = ButtonType.button, formMethod = ButtonFormMethod.post) {
                                i(classes = "fa-solid fa-reply")
                                +" Back"
                            }
                        }
                    }

                    div(classes = "center") {
                        p(classes = "title") {
                            +"Terms of Service"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }

                        p {
                            +"Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Vestibulum tortor quam, feugiat vitae, ultricies eget, tempor sit amet, ante. Donec eu libero sit amet quam egestas semper. Aenean ultricies mi vitae est. Mauris placerat eleifend leo. Quisque sit amet est et sapien ullamcorper pharetra. Vestibulum erat wisi, condimentum sed, commodo vitae, ornare sit amet, wisi. Aenean fermentum, elit eget tincidunt condimentum, eros ipsum rutrum orci, sagittis tempus lacus enim ac dui. Donec non enim in turpis pulvinar facilisis. Ut felis. Praesent dapibus, neque id cursus faucibus, tortor neque egestas augue, eu vulputate magna eros eu erat. Aliquam erat volutpat. Nam dui mi, tincidunt quis, accumsan porttitor, facilisis luctus, metus"
                        }
                    }
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

data class UserObj(@Expose val username: String, @Expose val passwordHash: String, @Expose val email: String)

data class Storage(@Expose var users: MutableList<UserObj>) {
    fun saveJson() {
        return File("./store.json").writeText(GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this))
    }

    fun getFromName(name: String): UserObj? {
        return users.find { it.username == name }
    }

    fun getFromEmail(name: String): UserObj? {
        return users.find { it.username == name }
    }

    companion object {
        fun loadJson(): Storage {
            val text = File("./store.json").readText()
            if (text.isEmpty()) {
                Storage(mutableListOf()).saveJson()
            }
            return GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().fromJson(text, Storage::class.java)
        }
    }
}
