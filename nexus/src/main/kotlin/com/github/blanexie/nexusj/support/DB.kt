package com.github.blanexie.nexusj.support

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.ktorm.logging.Slf4jLoggerAdapter
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("database")!!
private var database: Database? = null


fun database(): Database {
    if (database == null) {
        database = Database.connect(
            HikariDataSource(hikariConfig()),
            logger = Slf4jLoggerAdapter(logger)
        )
    }
    return database!!
}

fun hikariConfig(): HikariConfig {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl = setting.getStr("jdbcUrl")
    hikariConfig.username = setting.getStr("username")
    hikariConfig.password = setting.getStr("password")
    hikariConfig.driverClassName = setting.getStr("driverClassName")
    hikariConfig.minimumIdle = setting.getInt("minimumIdle")
    return hikariConfig
}


