package data.models.order

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING, CANCELLED, COLLECTED, COMPLETED
}

