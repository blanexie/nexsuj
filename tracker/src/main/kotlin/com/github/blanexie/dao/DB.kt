package com.github.blanexie.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.application.*
import org.ktorm.database.Database
import org.ktorm.support.mysql.MySqlDialect


val database = Database.connect(HikariDataSource(hikariConfig()), dialect = MySqlDialect())

fun hikariConfig(): HikariConfig {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl =
        "jdbc:mysql://68.79.59.185:8306/nexusj?allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai"
    hikariConfig.username = "xiezc"
    hikariConfig.password = "M93jj6fuSAd2"
    hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
    hikariConfig.minimumIdle = 3
    return hikariConfig
}