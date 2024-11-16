package data.models.user

import data.models.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID = UUID.randomUUID(),
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    var role: UserRole = UserRole.USER,
    var bonusPoints: Int = 0
)