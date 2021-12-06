package com.github.blanexie.nexusj.table

import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.singleOrNull
import org.ktorm.jackson.json
import org.ktorm.schema.Table
import org.ktorm.schema.int
import org.ktorm.schema.varchar


/**************** role ********************************************/
val Database.roleDO get() = this.sequenceOf(Role)

interface RoleDO : Entity<RoleDO> {
    companion object : Entity.Factory<UserDO>() {
        fun findById(id: Int): RoleDO? {
            return database().roleDO.singleOrNull { it.id eq id }
        }
    }

    var id: Int
    var name: String
    var attribute: Map<String, Any>

    fun save() {
        database().roleDO.add(this)
    }
}

object Role : Table<RoleDO>("role") {
    var id = int("id").primaryKey().bindTo { it.id }
    var name = varchar("name").bindTo { it.name }
    var attribute = json<Map<String, Any>>("attribute").bindTo { it.attribute }
}
