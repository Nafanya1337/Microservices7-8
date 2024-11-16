package controllers

import Order
import OrderItem
import data.models.UUIDSerializer
import data.models.order.OrderStatus
import kotlinx.serialization.Serializable
import repositories.OrderRepository
import java.util.*

class OrderController(
    private val orderRepository: OrderRepository
) {

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

    fun completeOrder(orderId: UUID): Order? {
        return orderRepository.updateOrderStatus(orderId, OrderStatus.COMPLETED)
    }

    fun rateOrder(orderId: UUID, rating: Int): Order? {

        if ((rating < 1 || rating > 5)) {
            throw InvalidRatingException()
        }

        val order = orderRepository.getOrderById(orderId)

        if (order?.status != OrderStatus.COMPLETED) {
            throw StateOrderException()
        }

        order.rating = rating

        return order
    }

    fun applyPromoCode(orderId: UUID, promoCode: String): Order? {
        if (!isValidPromoCode(promoCode)) {
            throw IllegalArgumentException("Invalid promo code")
        }
        val order = orderRepository.getOrderById(orderId)
        order?.promoCode = promoCode
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