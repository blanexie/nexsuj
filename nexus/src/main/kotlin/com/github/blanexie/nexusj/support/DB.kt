package com.github.blanexie.nexusj.support

import com.github.blanexie.dao.torrentDO
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.expression.ArgumentExpression
import org.ktorm.expression.BinaryExpression
import org.ktorm.expression.BinaryExpressionType
import org.ktorm.expression.ScalarExpression
import org.ktorm.logging.Slf4jLoggerAdapter
import org.ktorm.schema.BooleanSqlType
import org.ktorm.schema.ColumnDeclaring
import org.ktorm.schema.SqlType
import org.ktorm.support.mysql.MySqlDialect
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("database")!!
private var database: Database? = null



fun database(): Database {
    if (database == null) {
        database = Database.connect(
            HikariDataSource(hikariConfig()),
            dialect = MySqlDialect(),
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


data class ILikeExpression(
    val left: ScalarExpression<*>,
    val right: ScalarExpression<*>,
    override val sqlType: SqlType<Boolean> = BooleanSqlType,
    override val isLeafNode: Boolean = false,
    override val extraProperties: Map<String, Any> = emptyMap()
) : ScalarExpression<Boolean>()


