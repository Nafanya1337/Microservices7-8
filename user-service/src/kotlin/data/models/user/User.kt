package data.models.user

import UserRole
import data.models.UUIDSerializer
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class User(
    @Serializable(with = UUIDSerializer::class)
    val userId: UUID = UUID.randomUUID(),
    val fullName: String,
    val phoneNumber: String,
    val email: String,
    val password: String,
    val role: UserRole = UserRole.USER,
    val bonusPoints: Int = 0
)
