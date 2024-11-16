package data.models.user

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ADMIN, USER, MODERATOR;

    companion object {
        fun fromString(value: String): UserRole = values().first { it.name == value }
    }
}