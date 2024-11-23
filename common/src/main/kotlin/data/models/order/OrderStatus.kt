package data.models.order

import kotlinx.serialization.Serializable

@Serializable
enum class OrderStatus {
    PENDING, CANCELLED, COLLECTED, COMPLETED;

    companion object {
        fun fromString(value: String): OrderStatus = values().first { it.name == value }
    }
}

