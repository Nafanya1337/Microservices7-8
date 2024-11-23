package utils

import data.models.user.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.transactions.transaction
import utils.data.models.order.OrderItems
import utils.data.models.order.Orders

val dbHost = System.getenv("POSTGRES_HOST") ?: "postgres"
val dbPort = System.getenv("POSTGRES_PORT") ?: "5432"
val dbUser = System.getenv("POSTGRES_USER") ?: "postgres"
val dbPassword = System.getenv("POSTGRES_PASSWORD") ?: "1"
val dbName = System.getenv("POSTGRES_DB") ?: "ma_database"

object DatabaseFactory {
    fun initDatabase() {
        Database.connect(
            url = "jdbc:postgresql://$dbHost:$dbPort/$dbName",
            driver = "org.postgresql.Driver",
            user = dbUser,
            password = dbPassword
        )

        createTables()
    }

    private fun createTables() {
        transaction {
            val tables = listOf(Users, Orders, OrderItems)

            val missingTables = tables.filterNot { it.exists() }

            if (missingTables.isNotEmpty()) {
                SchemaUtils.create(*missingTables.toTypedArray())
            }
        }
    }
}
