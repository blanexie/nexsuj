package com.github.blanexie.nexusj.support

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.SqlType
import org.ktorm.support.mysql.MySqlDialect
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import kotlin.reflect.KClass


private var database: Database? = null

fun database(): Database {
    if (database == null) {
        database = Database.connect(HikariDataSource(hikariConfig()), dialect = MySqlDialect())
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


inline fun <reified C : Any> BaseTable<*>.json(name: String): Column<C> {
    return registerColumn(name, JsonSqlType(C::class))
}

class JsonSqlType<T : Any>(
    private val kClass: KClass<T>
) : SqlType<T>(Types.VARCHAR, "json") {

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        ps.setString(index, gson.toJson(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)
        return if (json.isNullOrBlank()) {
            null
        } else {
            gson.fromJson(json, kClass.java)
        }
    }
}