package com.example

import controllers.OrderController
import controllers.UserController
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.EngineMain
import io.ktor.server.plugins.contentnegotiation.*
import kotlinx.serialization.json.Json
import repositories.OrderRepository
import repositories.UserRepository

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module() {
    // Устанавливаем ContentNegotiation для обработки JSON
    install(ContentNegotiation) {
        json(Json {
            prettyPrint = true
            ignoreUnknownKeys = true
            encodeDefaults = true
        })
    }

    // Подключаем конфигурацию маршрутов
    configureRouting(OrderController(OrderRepository()), UserController(UserRepository()))
}
