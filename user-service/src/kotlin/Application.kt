import controllers.UserController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import repository.UserRepository

fun main(args: Array<String>) {
    DatabaseFactory.init()
    EngineMain.main(args)
}

fun Application.module() {
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    configureUserRouting(UserController(UserRepository()))
}
