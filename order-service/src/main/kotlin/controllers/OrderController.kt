package controllers

import Order
import OrderItem
import data.models.order.OrderStatus
import data.utils.UUIDSerializer
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import repositories.OrderRepository
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.serialization.kotlinx.json.*
import java.util.*
import kotlinx.serialization.Serializable

class OrderController(
    private val orderRepository: OrderRepository
) {

    val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    fun createOrder(request: CreateOrderRequest): Order {
        val order = Order(
            userId = request.userId,
            items = request.items
        )
        return orderRepository.createOrder(order)
    }

    fun cancelOrder(orderId: UUID): Order? {
        return orderRepository.updateOrderStatus(orderId, OrderStatus.CANCELLED)
    }

    fun collectOrder(orderId: UUID): Order? {
        return orderRepository.updateOrderStatus(orderId, OrderStatus.COLLECTED)
    }

    suspend fun completeOrder(orderId: UUID): Order? {
        val order = orderRepository.updateOrderStatus(orderId, OrderStatus.COMPLETED)
        if (order != null) {
            addBonuses(order.userId, calculateBonuses(order))
        }

        return order
    }

    private suspend fun addBonuses(userId: UUID, bonuses: Int) {
        try {
            client.patch("http://localhost:8080/users/$userId/add-bonuses") {
                contentType(ContentType.Application.Json)
                setBody(AddBonusesRequest(bonuses))
            }
        } catch (e: Exception) {
            println("Failed to add bonuses: ${e.message}")
        }
    }

    private fun calculateBonuses(order: Order): Int {
        return order.items.sumOf { it.quantity * 2 }
    }

    fun rateOrder(orderId: UUID, rating: Int): Order? {

        if ((rating < 1 || rating > 5)) {
            throw InvalidRatingException()
        }

        val order = orderRepository.getOrderById(orderId)

        if (order?.status != OrderStatus.COMPLETED) {
            throw StateOrderException()
        }

        orderRepository.updateOrderRating(orderId, rating)

        return order
    }

    fun applyPromoCode(orderId: UUID, promoCode: String): Order? {
        if (!isValidPromoCode(promoCode)) {
            throw IllegalArgumentException("Invalid promo code")
        }
        val order = orderRepository.getOrderById(orderId)
        orderRepository.updatePromoCode(orderId, promoCode)
        return order
    }

    fun getOrderById(orderId: UUID): Order? {
        return orderRepository.getOrderById(orderId)
    }

    private fun isValidPromoCode(promoCode: String): Boolean {
        return promoCode.matches(Regex("^[A-Z0-9]+$"))
    }

    @Serializable
    data class CreateOrderRequest(
        @Serializable(with = UUIDSerializer::class)
        val userId: UUID,
        val items: List<OrderItem>
    )
}

class StateOrderException : IllegalArgumentException("Order is not in a COMPLETED state")

class InvalidRatingException: IllegalArgumentException("Invalid rating")

@Serializable
data class AddBonusesRequest(val bonuses: Int)