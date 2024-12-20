import data.models.order.OrderStatus
import data.utils.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class Order(
    @Serializable(with = UUIDSerializer::class)
    val orderId: UUID = UUID.randomUUID(),
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID,
    var promoCode: String? = null,
    val items: List<OrderItem>,
    var status: OrderStatus = OrderStatus.PENDING,
    var rating: Int? = null,
    var appliedBonuses: Int = 0
)