import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import java.util.*

object Users : Table("users") {
    val userId: Column<UUID> = uuid("user_id").uniqueIndex()
    val fullName: Column<String> = varchar("full_name", 255)
    val phoneNumber: Column<String> = varchar("phone_number", 20)
    val email: Column<String> = varchar("email", 255)
    val password: Column<String> = varchar("password", 255)
    val role: Column<String> = varchar("role", 20).default(UserRole.USER.name)
    val bonusPoints: Column<Int> = integer("bonus_points").default(0)
}

@Serializable
enum class UserRole {
    ADMIN, USER, MODERATOR;

    companion object {
        fun fromString(value: String): UserRole = values().first { it.name == value }
    }
}