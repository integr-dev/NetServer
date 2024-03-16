package net.integr.plugins

import com.google.gson.GsonBuilder
import com.google.gson.annotations.Expose
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.html.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.ratelimit.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.get
import io.ktor.server.sessions.*
import io.ktor.util.*
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
                            +"function _0x1a4a(_0x34ab44,_0x269043){var _0x592d01=_0x592d();return _0x1a4a=function(_0x1a4a07,_0x405518){_0x1a4a07=_0x1a4a07-0x69;var _0x5dfd1f=_0x592d01[_0x1a4a07];return _0x5dfd1f;},_0x1a4a(_0x34ab44,_0x269043);}function _0x592d(){var _0x3ce7f5=['77zIPzaa','password','232242aQPtbG','value','318239QxMjZy','1422425vllBpr','2384RRznlj','6340630NlThIM','9qwyClr','getElementById','855132IELakW','ff594f8cf10ca2e3ad4279375f0d0e688a7eca861000e7ecc63ae4b105c8be7bcb57e8c1172ea460c462c6f715508dc356fd964cf41644682db1feffd466769a','2025240cSskPK','591MrjsPF'];_0x592d=function(){return _0x3ce7f5;};return _0x592d();}(function(_0x454e0c,_0x7d6a9a){var _0xae3a72=_0x1a4a,_0x42ef42=_0x454e0c();while(!![]){try{var _0x2d7388=parseInt(_0xae3a72(0x6e))/0x1+parseInt(_0xae3a72(0x70))/0x2*(parseInt(_0xae3a72(0x69))/0x3)+parseInt(_0xae3a72(0x74))/0x4+parseInt(_0xae3a72(0x6f))/0x5+-parseInt(_0xae3a72(0x6c))/0x6*(parseInt(_0xae3a72(0x6a))/0x7)+parseInt(_0xae3a72(0x76))/0x8+parseInt(_0xae3a72(0x72))/0x9*(-parseInt(_0xae3a72(0x71))/0xa);if(_0x2d7388===_0x7d6a9a)break;else _0x42ef42['push'](_0x42ef42['shift']());}catch(_0x42b8f5){_0x42ef42['push'](_0x42ef42['shift']());}}}(_0x592d,0x3bba6));function encrypt(){var _0x153436=_0x1a4a,_0x42162e=document['getElementById']('password')[_0x153436(0x6d)];if(_0x42162e['trim']()!==''){var _0x57c1fc=CryptoJS['MD5'](_0x42162e+_0x153436(0x75));document[_0x153436(0x73)](_0x153436(0x6b))[_0x153436(0x6d)]=_0x57c1fc;}}"
                        }
                        meta(name = "theme-color", content = "#199423")
                        meta("description", content = "NetServer Test")
                        meta(content = "NetServer", name = "og:title")
                        meta(content = "A test server by Integr", name = "og:description")
                        meta(content = "/logo.jpg", name = "og:image")
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
                                        id = "password"
                                        required = true
                                    }
                                }
                                p(classes = "pad") {
                                    button(classes = "btn btn-success", type = ButtonType.submit) {
                                        onClick = "encrypt()"
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
                        +"function _0x1a4a(_0x34ab44,_0x269043){var _0x592d01=_0x592d();return _0x1a4a=function(_0x1a4a07,_0x405518){_0x1a4a07=_0x1a4a07-0x69;var _0x5dfd1f=_0x592d01[_0x1a4a07];return _0x5dfd1f;},_0x1a4a(_0x34ab44,_0x269043);}function _0x592d(){var _0x3ce7f5=['77zIPzaa','password','232242aQPtbG','value','318239QxMjZy','1422425vllBpr','2384RRznlj','6340630NlThIM','9qwyClr','getElementById','855132IELakW','ff594f8cf10ca2e3ad4279375f0d0e688a7eca861000e7ecc63ae4b105c8be7bcb57e8c1172ea460c462c6f715508dc356fd964cf41644682db1feffd466769a','2025240cSskPK','591MrjsPF'];_0x592d=function(){return _0x3ce7f5;};return _0x592d();}(function(_0x454e0c,_0x7d6a9a){var _0xae3a72=_0x1a4a,_0x42ef42=_0x454e0c();while(!![]){try{var _0x2d7388=parseInt(_0xae3a72(0x6e))/0x1+parseInt(_0xae3a72(0x70))/0x2*(parseInt(_0xae3a72(0x69))/0x3)+parseInt(_0xae3a72(0x74))/0x4+parseInt(_0xae3a72(0x6f))/0x5+-parseInt(_0xae3a72(0x6c))/0x6*(parseInt(_0xae3a72(0x6a))/0x7)+parseInt(_0xae3a72(0x76))/0x8+parseInt(_0xae3a72(0x72))/0x9*(-parseInt(_0xae3a72(0x71))/0xa);if(_0x2d7388===_0x7d6a9a)break;else _0x42ef42['push'](_0x42ef42['shift']());}catch(_0x42b8f5){_0x42ef42['push'](_0x42ef42['shift']());}}}(_0x592d,0x3bba6));function encrypt(){var _0x153436=_0x1a4a,_0x42162e=document['getElementById']('password')[_0x153436(0x6d)];if(_0x42162e['trim']()!==''){var _0x57c1fc=CryptoJS['MD5'](_0x42162e+_0x153436(0x75));document[_0x153436(0x73)](_0x153436(0x6b))[_0x153436(0x6d)]=_0x57c1fc;}}"
                    }

                    meta(name = "theme-color", content = "#199423")
                    meta("description", content = "NetServer Test")
                    meta(content = "NetServer", name = "og:title")
                    meta(content = "A test server by Integr", name = "og:description")
                    meta(content = "/logo.jpg", name = "og:image")
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
                                    onClick = "encrypt()"
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

        rateLimit(RateLimitName("signup")) {
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

                if (call.sessions.get<UserSession>() == null) {
                    if (users.getFromName(username) == null) {
                        if (users.getFromEmail(email) == null) {
                            val user = UserObj(username, hash(password), email)
                            users.users += user
                            users.saveJson()

                            call.sessions.set(UserSession(username = username))
                            call.respondRedirect("/account")
                        } else {
                            call.respondRedirect("/signup?retry_signup=Email")
                        }
                    } else {
                        call.respondRedirect("/signup?retry_signup=Username")
                    }
                } else {
                    call.respondRedirect("/account")
                }
            }
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

                media(query = "only screen and (max-width: 600px)") {
                    rule(".center") {
                        margin = Margin(LinearDimension("5%"))
                        textAlign = TextAlign.start
                    }
                    rule(".login") {
                        margin = Margin(LinearDimension("2%"), LinearDimension("5%"))
                    }
                    rule(".pad") {
                        margin = Margin(LinearDimension("10px"), LinearDimension("5%"))
                    }
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
