import data.models.UUIDSerializer
import data.models.order.OrderStatus
import data.models.user.Users
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Orders : Table("orders") {
    val orderId: Column<UUID> = uuid("order_id").uniqueIndex()
    val userId: Column<UUID> = uuid("user_id").references(Users.userId)
    val promoCode: Column<String?> = varchar("promo_code", 50).nullable()
    val status: Column<String> = varchar("status", 20).default(OrderStatus.PENDING.name)
    val rating: Column<Int?> = integer("rating").nullable()
    val appliedBonuses: Column<Int> = integer("applied_bonuses").default(0)
    override val primaryKey = PrimaryKey(orderId) // Установить orderId в качестве PRIMARY KEY
}

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