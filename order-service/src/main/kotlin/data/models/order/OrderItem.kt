import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object OrderItems : Table("orders_items") {
    val orderId: Column<UUID> = uuid("order_id").references(Orders.orderId)
    val itemId: Column<Int> = integer("item_id")
    val quantity: Column<Int> = integer("quantity")

    override val primaryKey = PrimaryKey(orderId, itemId)
}

@Serializable
data class OrderItem(
    val itemId: Int,
    val quantity: Int
)
