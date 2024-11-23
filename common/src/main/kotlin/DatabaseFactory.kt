package utils

import org.jetbrains.exposed.sql.Database

object DatabaseFactory {
    fun initDatabase() {
        Database.connect(
            url = "jdbc:postgresql://localhost:5432/ma_database",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "1"
        )
    }
}
