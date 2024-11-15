package repositories

import data.models.user.User
import data.models.user.UserRole
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UserRepository {
    val users = ConcurrentHashMap<UUID, User>()

    fun createUser(user: User): User {
        users[user.userId] = user
        return user
    }

    fun getUserById(userId: UUID): User? {
        return users[userId]
    }

    fun assignRole(userId: UUID, role: UserRole): User? {
        val user = users[userId]
        user?.role = role
        return user
    }

    fun addBonuses(userId: UUID, bonuses: Int): User? {
        val user = users[userId]
        if (user != null) {
            user.bonusPoints += bonuses
        }
        return user
    }
}
