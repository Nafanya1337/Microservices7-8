package repositories

import Order
import data.models.order.OrderStatus
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class OrderRepository {
    private val orders = ConcurrentHashMap<UUID, Order>()

    fun createOrder(order: Order): Order {
        orders[order.orderId] = order
        return order
    }

    fun getOrderById(orderId: UUID): Order? = orders[orderId]

    fun updateOrderStatus(orderId: UUID, status: OrderStatus): Order? {
        val order = orders[orderId]
        if (order != null) {
            order.status = status
        }
        return order
    }
}
