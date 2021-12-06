package com.github.blanexie.nexusj.table

import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.ktorm.schema.*
import java.time.LocalDateTime

/*****************************************************/
val Database.userDO get() = this.sequenceOf(User)

interface UserDO : Entity<UserDO> {
    companion object : Entity.Factory<UserDO>() {
        fun findByIds(ids: List<Int>): List<UserDO> {
            return database().userDO.filter { it.id inList ids }.toList()
        }

        fun findByEmailAndPwd(email: String, pwd: String): UserDO? {
            return database().userDO.firstOrNull { (it.pwd eq pwd) and (it.email eq email) }
        }
    }

    var id: Int
    var email: String
    var pwd: String
    var nick: String
    var avatar: String?
    var sex: Int

    /**
     * 用户的角色id
     */
    var roleId: Int

    //上传量, 汇总的上传量
    var upload: Long

    //下载量,  汇总的下载量
    var download: Long

    //真实的上传
    var realUpload: Long

    // 真实的下载
    var realDownload: Long

    // 积分的大小,  总积分
    var integral: Long

    var createTime: LocalDateTime
    var updateTime: LocalDateTime

    /**
     *  0: 正常用户
     * -1: 永久封禁用户
     * -2: 限时封禁用户
     */
    var status: Int

    fun save() {
        database().userDO.add(this)
    }
}

object User : Table<UserDO>("user") {
    var id = int("id").primaryKey().bindTo { it.id }
    var email = varchar("email").bindTo { it.email }
    var pwd = varchar("pwd").bindTo { it.pwd }
    var sex = int("sex").bindTo { it.sex }
    var nick = varchar("nick").bindTo { it.nick }
    var avatar = varchar("avatar").bindTo { it.avatar }
    var roleId = int("role_id").bindTo { it.roleId }

    //上传量, 有效的上传量, 汇总的
    var upload = long("upload").bindTo { it.upload }

    //下载量, 有效的下载量, 汇总的
    var download = long("download").bindTo { it.download }


    // 积分的大小,
    var integral = long("integral").bindTo { it.integral }

    var createTime = datetime("create_time").bindTo { it.createTime }
    var updateTime = datetime("update_time").bindTo { it.updateTime }
    var status = int("status").bindTo { it.status }

}