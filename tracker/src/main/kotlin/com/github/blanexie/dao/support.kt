package com.github.blanexie.dao

import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column

import cn.hutool.core.bean.BeanUtil
import com.alibaba.fastjson.JSON
import org.ktorm.schema.SqlType
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Types
import kotlin.reflect.KClass

inline fun <reified C : Any> BaseTable<*>.json(name: String): Column<C> {
    return registerColumn(name, JsonSqlType(C::class))
}


class JsonSqlType<T : Any>(
    private val kClass: KClass<T>
) : SqlType<T>(Types.VARCHAR, "json") {

    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: T) {
        ps.setString(index, JSON.toJSONString(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): T? {
        val json = rs.getString(index)
        return if (json.isNullOrBlank()) {
            null
        } else {
            val json = JSON.parse(json) as JSON?
            JSON.toJavaObject(json, kClass.java)
        }
    }
}