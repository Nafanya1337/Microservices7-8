package repositories

import Order
import data.models.order.OrderStatus
import java.util.*

class OrderRepository {
    private val orders = mutableListOf<Order>()

    fun createOrder(order: Order): Order {
        orders.add(order)
        return order
    }

    fun getOrderById(id: UUID): Order? {
        return orders.find { it.id == id }
    }

    fun updateOrderStatus(id: UUID, status: OrderStatus): Order? {
        val order = getOrderById(id)
        order?.status = status
        return order
    }
}