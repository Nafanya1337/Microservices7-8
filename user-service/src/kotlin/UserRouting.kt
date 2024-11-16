import controllers.UserController
import data.models.user.User
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable
import java.util.*

fun Application.configureUserRouting(userController: UserController) {
    routing {
        post("/register") {
            val user = try {
                call.receive<User>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid input data")
                return@post
            }

            val createdUser = userController.registerUser(user)
            call.respond(HttpStatusCode.Created, "User ${createdUser.userId} created")
        }

        post("/login") {
            val loginRequest = call.receive<LoginRequest>()
            val user = userController.loginUser(loginRequest.phoneNumber, loginRequest.password)
            if (user != null) {
                call.respond(HttpStatusCode.OK, "User authorized successfully")
            } else {
                call.respond(HttpStatusCode.Unauthorized, "Invalid credentials")
            }
        }

        patch("/users/{userId}/assign-role") {
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid userId")
                return@patch
            }

            val assignRoleRequest = try {
                call.receive<AssignRoleRequest>()
            } catch (e: Exception) {
                call.respond(HttpStatusCode.BadRequest, "Invalid role data")
                return@patch
            }

            val updatedUser = userController.assignRole(userId, assignRoleRequest.role)
            if (updatedUser != null) {
                call.respond(HttpStatusCode.OK, "Role assigned successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }

        patch("/users/{userId}/add-bonuses") {
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid userId")
                return@patch
            }

            val addBonusesRequest = call.receive<AddBonusesRequest>()
            val updatedUser = userController.addBonuses(userId, addBonusesRequest.bonuses)
            if (updatedUser != null) {
                call.respond(HttpStatusCode.OK, "Bonuses added successfully")
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }

        get("/users/{userId}") {
            val userId = call.parameters["userId"]?.let { UUID.fromString(it) }
            if (userId == null) {
                call.respond(HttpStatusCode.BadRequest, "Missing or invalid userId")
                return@get
            }

            val user = userController.getUserById(userId)

            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound, "User not found")
            }
        }
    }
}

@Serializable
data class LoginRequest(val phoneNumber: String, val password: String)

@Serializable
data class AssignRoleRequest(val role: UserRole)

@Serializable
data class AddBonusesRequest(val bonuses: Int)
