import controllers.InvalidRatingException
import controllers.OrderController
import controllers.StateOrderException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

fun Application.configureOrderRouting(orderController: OrderController) {
    routing {
        post("/orders") {
            val request = try {
                call.receive<OrderController.CreateOrderRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid order data")
                return@post
            }

            val order = orderController.createOrder(request)
            call.respond(HttpStatusCode.Created, "Order: ${order.orderId} created successfully")
        }

        get("/orders/{orderId}") {
            val orderId = call.parameters["orderId"]?.let {
                try {
                    java.util.UUID.fromString(it)
                } catch (e: IllegalArgumentException) {
                    null
                }
            }

            if (orderId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid orderId")
                return@get
            }

            val order = orderController.getOrderById(orderId)
            if (order != null) {
                call.respond(HttpStatusCode.OK, order)
            } else {
                call.respond(HttpStatusCode.NotFound, "Order not found")
            }
        }

        patch("/orders/{orderId}/cancel") {
            val orderId = java.util.UUID.fromString(call.parameters["orderId"])
            val order = orderController.cancelOrder(orderId)
            if (order != null) {
                call.respond(HttpStatusCode.OK, "Order cancelled successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Order not found")
            }
        }

        patch("/orders/{orderId}/collect") {
            val orderId = java.util.UUID.fromString(call.parameters["orderId"])
            val order = orderController.collectOrder(orderId)
            if (order != null) {
                call.respond(HttpStatusCode.OK, "Order collected successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "Order not found")
            }
        }

        patch("/orders/{orderId}/complete") {
            val orderId = java.util.UUID.fromString(call.parameters["orderId"])
            val order = orderController.completeOrder(orderId)
            if (order != null) {
                call.respond(HttpStatusCode.OK, "Order completed successfully")

            } else {
                call.respond(HttpStatusCode.NotFound, "Order not found")
            }
        }

        patch("/orders/{orderId}/rate") {
            val orderId = java.util.UUID.fromString(call.parameters["orderId"])
            val request = call.receive<RateOrderRequest>()
            try {
                val order = orderController.rateOrder(orderId, request.rating)
                if (order != null) {
                    call.respond(HttpStatusCode.OK, "Order rated successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Order not found")
                }
            } catch (e: InvalidRatingException) {
                call.respond(HttpStatusCode.BadRequest, e.message!!)
            } catch (e: StateOrderException) {
                call.respond(HttpStatusCode.BadRequest, e.message!!)
            }
        }

        patch("/orders/{orderId}/apply-promocode") {
            val orderId = java.util.UUID.fromString(call.parameters["orderId"])
            val request = call.receive<ApplyPromoCodeRequest>()
            try {
                val order = orderController.applyPromoCode(orderId, request.promoCode)
                if (order != null) {
                    call.respond(HttpStatusCode.OK, "Promo code applied successfully")
                } else {
                    call.respond(HttpStatusCode.NotFound, "Order not found")
                }
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid promo code")
            }
        }
    }
}

@Serializable
data class RateOrderRequest(val rating: Int)

@Serializable
data class ApplyPromoCodeRequest(val promoCode: String)