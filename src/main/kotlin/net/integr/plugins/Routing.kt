@file:Suppress("SpellCheckingInspection", "DuplicatedCode")

package net.integr.plugins

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.sessions.*
import kotlinx.css.*
import kotlinx.html.*
import net.integr.UserSession
import net.integr.users
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Suppress("DEPRECATION")
fun Application.configureRouting() {
    routing {
        staticResources("/", "static")

        get("/") {
            call.log()
            call.respondRedirect("/login")
        }

        get("/login") {
            call.log()
            val userSession = call.sessions.get<UserSession>()
            if (userSession == null) {
                call.respondHtml {
                    lang = "en"
                    head {
                        title {
                            +"Login"
                        }

                        link(
                            rel = "stylesheet",
                            href = "https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css",
                            type = "text/css"
                        )
                        link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                        link(rel = "icon", type = "image/png", href = "/logo_round.png")

                        script {
                            src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                        }
                        script {
                            src = "https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"
                        }

                        script(
                            content = "(function(_0x13ed3e,_0x3dba20){const _0x58b0a7=_0x4009,_0x670e0d=_0x13ed3e();while(!![]){try{const _0x32b7d1=-parseInt(_0x58b0a7(0x1bc))/0x1+-parseInt(_0x58b0a7(0x1b3))/0x2*(parseInt(_0x58b0a7(0x1bd))/0x3)+parseInt(_0x58b0a7(0x1ae))/0x4+parseInt(_0x58b0a7(0x1b5))/0x5*(parseInt(_0x58b0a7(0x1b6))/0x6)+-parseInt(_0x58b0a7(0x1b4))/0x7*(-parseInt(_0x58b0a7(0x1af))/0x8)+parseInt(_0x58b0a7(0x1ba))/0x9*(parseInt(_0x58b0a7(0x1b2))/0xa)+parseInt(_0x58b0a7(0x1b0))/0xb;if(_0x32b7d1===_0x3dba20)break;else _0x670e0d['push'](_0x670e0d['shift']());}catch(_0x359fad){_0x670e0d['push'](_0x670e0d['shift']());}}}(_0x2bdd,0xdacbe));function _0x4009(_0x51b490,_0x49053e){const _0x2bdd30=_0x2bdd();return _0x4009=function(_0x4009eb,_0x3d7f62){_0x4009eb=_0x4009eb-0x1ae;let _0x367241=_0x2bdd30[_0x4009eb];return _0x367241;},_0x4009(_0x51b490,_0x49053e);}function _0x2bdd(){const _0x29b82b=['15370hFOxdx','1057iZvtkv','672630CgYntx','6HArxOK','fgFzp','toWellFormed','getElementById','936ZuajwO','form','751529gRINhb','579jBbvLx','3031256cppLQa','72016cZoAYy','8744318ClNRow','password','8110EpfdZQ'];_0x2bdd=function(){return _0x29b82b;};return _0x2bdd();}function handleEncrypt(){const _0x2018e8=_0x4009,_0x65d7c5={'fgFzp':_0x2018e8(0x1bb),'SrzZu':_0x2018e8(0x1b1)};if(document['getElementById'](_0x65d7c5[_0x2018e8(0x1b7)])['checkValidity']()){const _0x200726=document[_0x2018e8(0x1b9)](_0x65d7c5['SrzZu'])['value'];document['getElementById'](_0x65d7c5['SrzZu'])['value']=CryptoJS['MD5'](_0x200726[_0x2018e8(0x1b8)]());}}"
                        )

                        meta(name = "robots", content = "all")

                        meta(name = "theme-color", content = "#199423")
                        meta("description", content = "NetServer Test")
                        meta(content = "NetServer", name = "og:title")
                        meta(content = "A test server by Integr", name = "og:description")
                        meta(content = "/logo.jpg", name = "og:image")
                        meta(name = "viewport", content = "initial-scale=1, width=device-width")
                    }
                    body {
                        if (call.parameters.contains("logging_out")) {
                            div(classes = "pad_offset") {
                                div(classes = "alert alert-success alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Successfully logged out!"
                                    a(href = "/login", classes = "color_inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        if (call.parameters.contains("retry_login")) {
                            div(classes = "pad_offset") {
                                div(classes = "alert alert-warning alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Username and/or Password incorrect!"
                                    a(href = "/login", classes = "color_inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        if (call.parameters.contains("not_logged_in")) {
                            div(classes = "pad_offset") {
                                div(classes = "alert alert-warning alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Please sign in first!"
                                    a(href = "/login", classes = "color_inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        if (call.parameters.contains("deleted")) {
                            div(classes = "pad_offset") {
                                div(classes = "alert alert-success alert-dismissible fade show") {
                                    i(classes = "fa-solid fa-triangle-exclamation")
                                    +" Successfully deleted your account!"
                                    a(href = "/login", classes = "color_inherit") {
                                        button(type = ButtonType.button, classes = "close") {
                                            i(classes = "fa-solid fa-xmark")
                                        }
                                    }
                                }
                            }
                        }

                        form(
                            action = "/login_internal",
                            encType = FormEncType.multipartFormData,
                            method = FormMethod.post
                        ) {
                            id = "form"
                            div(classes = "area_center") {
                                img(classes = "img", src = "/logo.jpg", alt = "Logo")
                                p(classes = "title_center") {
                                    +"NetServer"
                                }
                                p(classes = "pad_center") {
                                    +"Username"
                                    textInput(name = "username", classes = "form-control") {
                                        required = true
                                    }
                                }
                                p(classes = "pad_center") {
                                    +"Password"
                                    passwordInput(name = "password", classes = "form-control") {
                                        id = "password"
                                        required = true
                                    }
                                }
                                p(classes = "pad_center") {
                                    button(classes = "btn btn-success", type = ButtonType.submit) {
                                        onClick = "handleEncrypt()"
                                        i(classes = "fa-solid fa-right-to-bracket")
                                        +" Login"
                                    }

                                    a(href = "/signup", classes = "pad_offset_horizontal") {
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
            call.log()
            call.respondHtml {
                lang = "en"
                head {
                    title {
                        +"Signup"
                    }

                    link(
                        rel = "stylesheet",
                        href = "https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css",
                        type = "text/css"
                    )
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    link(rel = "icon", type = "image/png", href = "/logo_round.png")

                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    script {
                        src = "https://cdnjs.cloudflare.com/ajax/libs/crypto-js/3.1.2/rollups/aes.js"
                    }

                    script(
                        content = "(function(_0x13ed3e,_0x3dba20){const _0x58b0a7=_0x4009,_0x670e0d=_0x13ed3e();while(!![]){try{const _0x32b7d1=-parseInt(_0x58b0a7(0x1bc))/0x1+-parseInt(_0x58b0a7(0x1b3))/0x2*(parseInt(_0x58b0a7(0x1bd))/0x3)+parseInt(_0x58b0a7(0x1ae))/0x4+parseInt(_0x58b0a7(0x1b5))/0x5*(parseInt(_0x58b0a7(0x1b6))/0x6)+-parseInt(_0x58b0a7(0x1b4))/0x7*(-parseInt(_0x58b0a7(0x1af))/0x8)+parseInt(_0x58b0a7(0x1ba))/0x9*(parseInt(_0x58b0a7(0x1b2))/0xa)+parseInt(_0x58b0a7(0x1b0))/0xb;if(_0x32b7d1===_0x3dba20)break;else _0x670e0d['push'](_0x670e0d['shift']());}catch(_0x359fad){_0x670e0d['push'](_0x670e0d['shift']());}}}(_0x2bdd,0xdacbe));function _0x4009(_0x51b490,_0x49053e){const _0x2bdd30=_0x2bdd();return _0x4009=function(_0x4009eb,_0x3d7f62){_0x4009eb=_0x4009eb-0x1ae;let _0x367241=_0x2bdd30[_0x4009eb];return _0x367241;},_0x4009(_0x51b490,_0x49053e);}function _0x2bdd(){const _0x29b82b=['15370hFOxdx','1057iZvtkv','672630CgYntx','6HArxOK','fgFzp','toWellFormed','getElementById','936ZuajwO','form','751529gRINhb','579jBbvLx','3031256cppLQa','72016cZoAYy','8744318ClNRow','password','8110EpfdZQ'];_0x2bdd=function(){return _0x29b82b;};return _0x2bdd();}function handleEncrypt(){const _0x2018e8=_0x4009,_0x65d7c5={'fgFzp':_0x2018e8(0x1bb),'SrzZu':_0x2018e8(0x1b1)};if(document['getElementById'](_0x65d7c5[_0x2018e8(0x1b7)])['checkValidity']()){const _0x200726=document[_0x2018e8(0x1b9)](_0x65d7c5['SrzZu'])['value'];document['getElementById'](_0x65d7c5['SrzZu'])['value']=CryptoJS['MD5'](_0x200726[_0x2018e8(0x1b8)]());}}"
                    )

                    meta(name = "robots", content = "all")

                    meta(name = "theme-color", content = "#199423")
                    meta("description", content = "NetServer Test")
                    meta(content = "NetServer", name = "og:title")
                    meta(content = "A test server by Integr", name = "og:description")
                    meta(content = "/logo.jpg", name = "og:image")
                    meta(name = "viewport", content = "initial-scale=1, width=device-width")
                }
                body {
                    if (call.parameters.contains("retry_signup")) {
                        val errorRegion = call.parameters["retry_signup"]
                        div(classes = "pad_offset") {
                            div(classes = "alert alert-warning alert-dismissible fade show") {
                                i(classes = "fa-solid fa-triangle-exclamation")
                                +" $errorRegion not accepted!"
                                a(href = "/signup", classes = "color_inherit") {
                                    button(type = ButtonType.button, classes = "close") {
                                        i(classes = "fa-solid fa-xmark")
                                    }
                                }
                            }
                        }
                    }

                    form(classes = "form-inline") {
                        a(href = "/login", classes = "pad_offset") {
                            button(
                                classes = "btn btn-warning",
                                type = ButtonType.button,
                                formMethod = ButtonFormMethod.post
                            ) {
                                i(classes = "fa-solid fa-reply")
                                +" Back"
                            }
                        }
                    }

                    form(
                        action = "/signup_internal",
                        encType = FormEncType.multipartFormData,
                        method = FormMethod.post
                    ) {
                        id = "form"
                        div(classes = "login") {
                            img(classes = "img", src = "/logo.jpg", alt = "Logo")

                            p(classes = "title_center") {
                                +"NetServer Signup"
                            }

                            p(classes = "pad_center") {
                                +"Username"
                                textInput(name = "username", classes = "form-control") {
                                    required = true
                                }
                            }

                            p(classes = "pad_center") {
                                +"Email"
                                emailInput(name = "email", classes = "form-control") {
                                    required = true
                                }
                            }

                            p(classes = "pad_center") {
                                +"Password"
                                passwordInput(name = "password", classes = "form-control") {
                                    required = true
                                    id = "password"
                                }
                            }

                            p(classes = "pad_center") {
                                checkBoxInput(name = "agree_tos") {
                                    required = true
                                }

                                +" I agree to the "
                                a(href = "/tos") { +"Terms of Service" }
                            }

                            p(classes = "pad_center") {
                                button(classes = "btn btn-warning", type = ButtonType.submit) {
                                    onClick = "handleEncrypt()"
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
            call.log()
            if (call.sessions.get<UserSession>() == null) call.respondRedirect("/login?not_logged_in")

            call.respondHtml {
                lang = "en"
                head {
                    title {
                        +"Account"
                    }
                    link(
                        rel = "stylesheet",
                        href = "https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css",
                        type = "text/css"
                    )
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    link(rel = "icon", type = "image/png", href = "/logo_round.png")
                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    meta("description", content = "NetServer Test")
                    meta(name = "viewport", content = "initial-scale=1, width=device-width")
                }
                body {
                    form(classes = "form-inline") {
                        a(href = "/logout_internal", classes = "pad_offset") {
                            button(classes = "btn btn-warning", type = ButtonType.button) {
                                i(classes = "fa-solid fa-right-from-bracket")
                                +" Logout"
                            }
                        }

                        a(href = "/delete_internal", classes = "pad_offset") {
                            button(classes = "btn btn-danger", type = ButtonType.button) {
                                i(classes = "fa-solid fa-trash")
                                +" Delete account"
                            }
                        }
                    }

                    div(classes = "area_center") {
                        p(classes = "title_center") {
                            +"Welcome ${call.sessions.get<UserSession>()!!.username}"
                        }
                    }
                }
            }
        }

        get("/delete_internal") {
            call.log()
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
            call.log()
            val data = call.receiveMultipart()
            var username = ""
            var password = ""

            data.forEachPart { part ->
                if (part is PartData.FormItem) {
                    if (part.name == "username") username = part.value
                    if (part.name == "password") password = part.value
                }
            }

            /* Check if the given password is a valid hash */
            if (validateMD5(password)) {
                /* Check if the given username exists */
                if (users.getFromName(username) != null) {
                    val salt = users.getFromName(username)!!.salt

                    /* Check if password hash in the database is the password + the saved salt */
                    if (users.getFromName(username)!!.passwordHash == hash(password + salt)) {
                        call.sessions.set(UserSession(username = username)) // Save cookie
                        call.respondRedirect("/account")
                    }
                } else call.respondRedirect("/login?retry_login")
            } else call.respondRedirect("/login")

        }

        rateLimit(RateLimitName("signup")) {
            post("/signup_internal") {
                call.log()
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

                /* Check if the given password is a valid hash */
                if (validateMD5(password)) {
                    /* Check if the client is not signed in */
                    if (call.sessions.get<UserSession>() == null) {
                        /* Check if the username is not taken */
                        if (users.getFromName(username) == null) {
                            /* Check if the email is not taken */
                            if (users.getFromEmail(email) == null) {
                                val salt = generateSalt()
                                val user = UserObj(username, hash(password + salt), salt, email) // Generate user
                                users.users += user
                                users.saveJson()

                                call.sessions.set(UserSession(username = username)) // Save cookie
                                call.respondRedirect("/account")
                            } else call.respondRedirect("/signup?retry_signup=Email")
                        } else call.respondRedirect("/signup?retry_signup=Username")
                    } else call.respondRedirect("/account")
                } else call.respondRedirect("/signup?retry_signup=Password")
            }
        }

        get("/logout_internal") {
            call.log()
            if (call.sessions.get<UserSession>() != null) {
                call.sessions.clear<UserSession>()
            }

            call.respondRedirect("/login?logging_out")
        }

        get("/styles.css") {
            call.respondCss {
                /* Center an element horizontally */
                rule(".pad_center") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                }

                /* Apply offsets of 10px to an element */
                rule(".pad_offset") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("10px"))
                    display = Display.block
                }

                /* Apply horizontal only offsets of 10px to an element */
                rule(".pad_offset_horizontal") {
                    margin = Margin(LinearDimension("0px"), LinearDimension("10px"))
                }

                /* Inherit the color of the parent */
                rule(".color_inherit") {
                    color = Color.inherit
                }

                /* Center a form */
                rule(".area_center") {
                    margin = Margin(LinearDimension("5%"), LinearDimension("20%"))
                }

                rule("html") {
                    height = LinearDimension("100%")
                    width = LinearDimension("100%")
                }

                /* Center a large text */
                rule(".text_center") {
                    margin = Margin(LinearDimension("20%"))
                    textAlign = TextAlign.start
                }

                /* Center a title */
                rule(".title_center") {
                    margin = Margin(LinearDimension("10px"), LinearDimension("30%"))
                    fontSize = LinearDimension("50px")
                    textAlign = TextAlign.center
                    fontWeight = FontWeight.bolder
                }

                media(query = "only screen and (max-width: 1000px)") {
                    rule(".text_center") {
                        margin = Margin(LinearDimension("5%"))
                        textAlign = TextAlign.start
                    }

                    rule(".area_center") {
                        margin = Margin(LinearDimension("2%"), LinearDimension("5%"))
                    }

                    rule(".pad_center") {
                        margin = Margin(LinearDimension("10px"), LinearDimension("5%"))
                    }

                    rule(".title_center") {
                        margin = Margin(LinearDimension("10px"), LinearDimension("5%"))
                        fontSize = LinearDimension("50px")
                        textAlign = TextAlign.center
                        fontWeight = FontWeight.bolder
                    }
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

        get("/tos") {
            call.log()
            call.respondHtml {
                head {
                    title {
                        +"Terms of Service"
                    }
                    link(
                        rel = "stylesheet",
                        href = "https://cdn.jsdelivr.net/npm/bootstrap@4.3.1/dist/css/bootstrap.min.css",
                        type = "text/css"
                    )
                    link(rel = "stylesheet", href = "/styles.css", type = "text/css")
                    link(rel = "icon", type = "image/png", href = "/logo_round.png")
                    script {
                        src = "https://kit.fontawesome.com/0a7e2ccef9.js"
                    }
                    meta("viewport", content = "width=device-width, initial-scale=1")
                }
                body {
                    form(classes = "form-inline") {
                        a(href = "/signup", classes = "pad_offset") {
                            button(
                                classes = "btn btn-warning",
                                type = ButtonType.button,
                                formMethod = ButtonFormMethod.post
                            ) {
                                i(classes = "fa-solid fa-reply")
                                +" Back"
                            }
                        }
                    }

                    div(classes = "area_center") {
                        p(classes = "title_center") {
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

fun hashOnce(password: String): String {
    return BigInteger(1, MessageDigest.getInstance("SHA-512").digest(password.toByteArray())).toString(16)
}

fun hash(password: String): String {
    var hash = password

    for (i in 1..32) {
        hash = hashOnce(hash)
    }

    return hash
}

fun validateMD5(password: String): Boolean {
    if (password.length != 32) return false
    if (!password.matches(Regex("[0-9a-f]{32}"))) return false

    return true
}

fun generateSalt(): String {
    val random = SecureRandom()
    val bytes = ByteArray(32)
    random.nextBytes(bytes)
    return hash(bytes.joinToString("").filter { c -> c != '-' })
}

fun ApplicationCall.log() {
    println(
        "\nCall to " + this.request.origin.uri
                + "\n* IP: " + this.request.origin.remoteAddress
                + "\n* PORT: " + this.request.origin.remotePort
                + "\n* METHOD: " + this.request.origin.method.value
                + "\n* VERSION: " + this.request.origin.version
                + "\n* TIMESTAMP: " + LocalTime.now().format(DateTimeFormatter.ISO_TIME)
    )
}

data class UserObj(
    @Expose val username: String,
    @Expose val passwordHash: String,
    @Expose val salt: String,
    @Expose val email: String
)

data class Storage(@Expose var users: MutableList<UserObj>) {
    fun saveJson() {
        return File("./store.json").writeText(
            GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create().toJson(this)
        )
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
            return GsonBuilder().setPrettyPrinting().excludeFieldsWithoutExposeAnnotation().create()
                .fromJson(text, Storage::class.java)
        }
    }
}
