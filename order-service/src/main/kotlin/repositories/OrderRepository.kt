package repositories

import Order
import OrderItem
import data.models.order.OrderStatus
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import utils.data.models.order.OrderItems
import utils.data.models.order.Orders
import java.util.*

class OrderRepository {

    fun createOrder(order: Order): Order {
        transaction {
            Orders.insert {
                it[orderId] = order.orderId
                it[userId] = order.userId
                it[promoCode] = order.promoCode
                it[status] = order.status.name
                it[rating] = order.rating
                it[appliedBonuses] = order.appliedBonuses
            }

            order.items.forEach { item ->
                OrderItems.insert {
                    it[orderId] = order.orderId
                    it[itemId] = item.itemId.toInt()
                    it[quantity] = item.quantity
                }
            }
        }
        return order
    }

    fun getOrderById(orderId: UUID): Order? {
        return transaction {
            val orderRow = Orders.select { Orders.orderId eq orderId }
                .singleOrNull() ?: return@transaction null

            val items = OrderItems.select { OrderItems.orderId eq orderId }
                .map {
                    OrderItem(
                        itemId = it[OrderItems.itemId],
                        quantity = it[OrderItems.quantity]
                    )
                }

            Order(
                orderId = orderRow[Orders.orderId],
                userId = orderRow[Orders.userId],
                promoCode = orderRow[Orders.promoCode],
                status = OrderStatus.valueOf(orderRow[Orders.status]),
                rating = orderRow[Orders.rating],
                appliedBonuses = orderRow[Orders.appliedBonuses],
                items = items
            )
        }
    }

    fun updateOrderStatus(orderId: UUID, status: OrderStatus): Order? {
        transaction {
            Orders.update({ Orders.orderId eq orderId }) {
                it[Orders.status] = status.name
            }
        }
        return getOrderById(orderId)
    }

    fun updateOrderRating(orderId: UUID, rating: Int) {
        transaction {
            Orders.update({ Orders.orderId eq orderId }) {
                it[Orders.rating] = rating
            }
        }
    }

    fun updatePromoCode(orderId: UUID, promoCode: String) {
        transaction {
            Orders.update({ Orders.orderId eq orderId }) {
                it[Orders.promoCode] = promoCode
            }
        }
    }
}

