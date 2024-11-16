package controllers

import UserRole
import data.models.user.User
import repository.UserRepository
import java.util.UUID

class UserController(private val userRepository: UserRepository) {

    fun registerUser(user: User): User {
        return userRepository.createUser(user)
    }

    fun loginUser(phoneNumber: String, password: String): User? {
        return userRepository.getUserByPhoneAndPassword(phoneNumber, password)
    }

    fun assignRole(userId: UUID, role: UserRole): User? {
        return userRepository.assignRole(userId, role)
    }

    fun addBonuses(userId: UUID, bonuses: Int): User? {
        return userRepository.addBonuses(userId, bonuses)
    }

    fun getUserById(userId: UUID): User? {
        return userRepository.getUserById(userId)
    }
}
