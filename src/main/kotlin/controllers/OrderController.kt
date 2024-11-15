package controllers

import Order
import data.models.order.OrderStatus
import repositories.OrderRepository
import java.util.*

class OrderController(private val orderRepository: OrderRepository) {

    fun createOrder(order: Order): Order {
        return orderRepository.createOrder(order)
    }

    fun getOrder(id: UUID): Order? {
        return orderRepository.getOrderById(id)
    }

    fun updateOrderStatus(id: UUID, status: OrderStatus): Order? {
        return processOrderUpdate(id, status)
    }

    private fun processOrderUpdate(id: UUID, status: OrderStatus): Order? {
        return orderRepository.updateOrderStatus(id, status)
    }
}