package ru.smak.net


import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager
import java.util.Date


fun main() {
    val oper = MySqlOperator.getInstance()
    oper.createTables()

}

class MySqlOperator private constructor() {
    val login = "postgres"
    val password = "1234"

    init {
        try {
            Database.connect(
                {
                    DriverManager.getConnection("jdbc:postgresql://localhost:5433/chat", login, password)
                }
            )
            println("Connection successful to the database")
        } catch (e: Exception) {
            println(e)
            println("You didnt connect to the database")
        }
    }

    object Messages : IntIdTable("messages") {
        val message = varchar("message", 255)
        val recipient = varchar("receiver", 50)
        val sender = varchar("sender", 50)
        val date = Date()
    }

    fun createTables() {
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.createMissingTablesAndColumns(Messages)
        }
    }

    companion object {
        val inst = MySqlOperator()
        fun getInstance(): MySqlOperator {
            return inst
        }
    }

}
