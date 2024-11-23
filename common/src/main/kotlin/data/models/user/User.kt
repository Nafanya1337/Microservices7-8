package data.models.user

import data.utils.UUIDSerializer
import org.jetbrains.exposed.sql.Table
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import java.util.*

object Users : Table("users") {
    val userId: Column<UUID> = uuid("user_id").uniqueIndex()
    val fullName: Column<String> = varchar("full_name", 255)
    val phoneNumber: Column<String> = varchar("phone_number", 20)
    val email: Column<String> = varchar("email", 255)
    val password: Column<String> = varchar("password", 255)
    val role: Column<String> = varchar("role", 20).default(UserRole.USER.name) // Используем строку
    val bonusPoints: Column<Int> = integer("bonus_points").default(0)
}

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
