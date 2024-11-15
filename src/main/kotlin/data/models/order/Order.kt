import data.models.order.OrderStatus
import java.util.*

data class Order(
    val id: UUID,
    val userId: UUID,
    var status: OrderStatus,
    val items: List<OrderItem>
)