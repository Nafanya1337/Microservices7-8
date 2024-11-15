package data.models.user

import kotlinx.serialization.Serializable

@Serializable
enum class UserRole {
    ADMIN, USER, MODERATOR
}