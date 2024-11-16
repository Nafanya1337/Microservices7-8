package repositories

import data.models.user.User
import data.models.user.UserRole
import data.models.user.Users
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.*

class UserRepository {

    fun createUser(user: User): User {
        transaction {
            Users.insert {
                it[userId] = user.userId
                it[fullName] = user.fullName
                it[phoneNumber] = user.phoneNumber
                it[email] = user.email
                it[password] = user.password
                it[role] = user.role.name
                it[bonusPoints] = user.bonusPoints
            }
        }
        return user
    }

    fun getUserById(userId: UUID): User? {
        return transaction {
            Users.select { Users.userId eq userId }
                .mapNotNull {
                    User(
                        userId = it[Users.userId],
                        fullName = it[Users.fullName],
                        phoneNumber = it[Users.phoneNumber],
                        email = it[Users.email],
                        password = it[Users.password],
                        role = UserRole.valueOf(it[Users.role]),
                        bonusPoints = it[Users.bonusPoints]
                    )
                }
                .singleOrNull()
        }
    }

    fun assignRole(userId: UUID, role: UserRole): User? {
        transaction {
            Users.update({ Users.userId eq userId }) {
                it[Users.role] = role.name
            }
        }
        return getUserById(userId)
    }

    fun addBonuses(userId: UUID, bonuses: Int): User? {
        transaction {
            Users.update({ Users.userId eq userId }) {
                with(SqlExpressionBuilder) {
                    it.update(Users.bonusPoints, Users.bonusPoints + bonuses)
                }
            }
        }
        return getUserById(userId)
    }

    fun getUserByPhoneAndPassword(phoneNumber: String, password: String): User? {
        return transaction {
            Users.select { (Users.phoneNumber eq phoneNumber) and (Users.password eq password) }
                .mapNotNull {
                    User(
                        userId = it[Users.userId],
                        fullName = it[Users.fullName],
                        phoneNumber = it[Users.phoneNumber],
                        email = it[Users.email],
                        password = it[Users.password],
                        role = UserRole.valueOf(it[Users.role]),
                        bonusPoints = it[Users.bonusPoints]
                    )
                }
                .singleOrNull()
        }
    }
}