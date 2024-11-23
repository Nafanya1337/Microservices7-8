package controllers

import data.models.user.User
import data.models.user.UserRole
import repositories.UserRepository
import routes.RegistrationRequest
import java.util.UUID

class UserController(private val userRepository: UserRepository) {

    fun registerUser(registrationRequest: RegistrationRequest): User {
        return userRepository.createUser(registrationRequest)
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
