import kotlinx.serialization.Serializable

@Serializable
data class OrderItem(
    val itemId: Int,
    val quantity: Int
)
