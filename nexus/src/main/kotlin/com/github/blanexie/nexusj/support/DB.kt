package com.github.blanexie.dao

import com.github.blanexie.nexusj.setting
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect


val database = Database.connect(HikariDataSource(hikariConfig()), dialect = MySqlDialect())

fun hikariConfig(): HikariConfig {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl = setting.getStr("jdbcUrl")
    hikariConfig.username = setting.getStr("username")
    hikariConfig.password = setting.getStr("password")
    hikariConfig.driverClassName = setting.getStr("driverClassName")
    hikariConfig.minimumIdle = setting.getInt("minimumIdle")
    return hikariConfig
}