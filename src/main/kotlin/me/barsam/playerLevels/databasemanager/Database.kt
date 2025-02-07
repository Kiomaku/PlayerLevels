package me.barsam.playerLevels.databasemanager

import me.barsam.playerLevels.PlayerLevels
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.io.File

object Database {
    private lateinit var connection: Connection

    fun initDatabase() {
        val config = PlayerLevels.instance
        val databaseType = config.getCachedConfigValue("database.type") as? String ?: "mysql"

        try {
            connection = when (databaseType.lowercase()) {
                "sqlite" -> initSQLite(config.getCachedConfigValue("database.sqlite.file") as? String ?: "playerlevels.db")
                "mysql" -> initMySQL(
                    config.getCachedConfigValue("database.mysql.host") as? String ?: "localhost",
                    (config.getCachedConfigValue("database.mysql.port") as? Int) ?: 3306,
                    config.getCachedConfigValue("database.mysql.database") as? String ?: "minecraft",
                    config.getCachedConfigValue("database.mysql.user") as? String ?: "root",
                    config.getCachedConfigValue("database.mysql.password") as? String ?: "password"
                )
                else -> throw IllegalArgumentException("Invalid database type in config.yml")
            }

            createTable()
            println("$databaseType Database Connected Successfully!")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initMySQL(host: String, port: Int, database: String, user: String, password: String): Connection {
        return DriverManager.getConnection("jdbc:mysql://$host:$port/$database?useSSL=false", user, password)
    }

    private fun initSQLite(fileName: String): Connection {
        val dbFile = File(PlayerLevels.instance.dataFolder, fileName)
        return DriverManager.getConnection("jdbc:sqlite:${dbFile.absolutePath}")
    }

    private fun createTable() {
        val statement = connection.createStatement()
        statement.executeUpdate(
            """
            CREATE TABLE IF NOT EXISTS player_levels (
                uuid TEXT PRIMARY KEY,
                xp INT DEFAULT 0,
                level INT DEFAULT 0
            )
            """
        )
    }

    fun getConnection(): Connection = connection

    fun closeConnection() {
        try {
            if (::connection.isInitialized && !connection.isClosed) {
                connection.close()
                println("Database Connection Closed Successfully!")
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        }
    }
}
