package com.github.blanexie.nexusj.table

import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.singleOrNull
import org.ktorm.schema.*
import java.time.LocalDateTime

/*****************************************************/
val Database.userTorrentDO get() = this.sequenceOf(UserTorrent)


interface UserTorrentDO : Entity<UserTorrentDO> {
    companion object : Entity.Factory<UserTorrentDO>() {
        fun findById(id: Int): UserTorrentDO? {
            return database().userTorrentDO.singleOrNull { it.id eq id }
        }

        fun findByInfoHashAndUserId(infoHash: String, userId: Int): UserTorrentDO? {
            return database().userTorrentDO.singleOrNull { (it.infoHash eq infoHash) and (it.userId eq userId) }
        }

        fun findByInfoHashAndAuthKey(infoHash: String, authKey: String): UserTorrentDO? {
            return database().userTorrentDO.singleOrNull { (it.infoHash eq infoHash) and (it.authKey eq authKey) }
        }
    }

    var id: Int
    var userId: Int
    var infoHash: String
    var createTime: LocalDateTime
    var authKey: String

    //当前用户 当前种子的邮箱上传量
    var upload: Long

    //当前用户 当前种子的有效下载量
    var download: Long

    //真实的上传
    var realUpload: Long

    // 真实的下载
    var realDownload: Long

    //0 : 正常默认收藏这个种子状态
    //1 : 当前用户下载这个种子文件
    //2 : 当前用户是这个种子的发种者
    var status: Int

    fun save() {
        database().userTorrentDO.add(this)
    }
}

object UserTorrent : Table<UserTorrentDO>("user_torrent") {
    var id = int("id").primaryKey().bindTo { it.id }
    var userId = int("user_id").bindTo { it.userId }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var createTime = datetime("create_time").bindTo { it.createTime }
    var authKey = varchar("auth_key").bindTo { it.authKey }

    //当前用户 当前种子的邮箱上传量
    var upload = long("upload").bindTo { it.upload }

    //当前用户 当前种子的有效下载量
    var download = long("download").bindTo { it.download }

    //真实的上传
    var realUpload = long("real_upload").bindTo { it.realUpload }

    // 真实的下载
    var realDownload = long("real_download").bindTo { it.realDownload }

    var status = int("status").bindTo { it.status }
}

