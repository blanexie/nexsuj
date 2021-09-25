package com.github.blanexie.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database


val database = Database.connect(HikariDataSource(hikariConfig()))

fun hikariConfig(): HikariConfig {
    val hikariConfig = HikariConfig()
    hikariConfig.jdbcUrl =
        "jdbc:mysql://192.168.0.106:8306/nexusj?allowPublicKeyRetrieval=true&serverTimezone=Asia/Shanghai"
    hikariConfig.username = "xiezc"
    hikariConfig.password = "M93jj6fuSAd2"
    hikariConfig.driverClassName = "com.mysql.cj.jdbc.Driver"
    return hikariConfig
}