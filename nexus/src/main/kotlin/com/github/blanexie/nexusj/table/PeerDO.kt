package com.github.blanexie.nexusj.table

import com.github.blanexie.nexusj.controller.peerEvent
import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.inList
import org.ktorm.entity.*
import org.ktorm.schema.*
import java.time.LocalDateTime

/********************************************************/
val Database.peerDO get() = this.sequenceOf(Peer)

interface PeerDO : Entity<PeerDO> {
    companion object : Entity.Factory<PeerDO>() {

        fun findByInfoHashAndEventIn(infoHash: String, peerEvent: List<String>): List<PeerDO> {
            return database().peerDO.filter {
                (it.infoHash eq infoHash) and (it.event inList peerEvent)
            }.toList()
        }
    }

    var id: Int
    var infoHash: String
    var peerId: String
    var port: Int
    var uploaded: Long
    var downloaded: Long
    var left: Long
    var compact: Int
    var event: String
    var ip: String?
    var ipv6: String?
    var numwant: Int
    var trackerid: String?
    var authKey: String

    var createTime: LocalDateTime
    var reportTime: LocalDateTime

    //默认0 , 如果-1 就是表示这个peerId被禁用了
    var status: Int

    fun save() {
        database().peerDO.add(this)
    }

    fun update() {
        this.id!!
        database().peerDO.update(this)
    }
}


object Peer : Table<PeerDO>("peer") {
    var id = int("id").primaryKey().bindTo { it.id }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var peerId = varchar("peer_id").bindTo { it.peerId }
    var port = int("port").bindTo { it.port }
    var uploaded = long("uploaded").bindTo { it.uploaded }
    var downloaded = long("downloaded").bindTo { it.downloaded }
    var left = long("left").bindTo { it.left }
    var compact = int("compact").bindTo { it.compact }
    var event = varchar("event").bindTo { it.event }
    var ip = varchar("ip").bindTo { it.ip }
    var ipv6 = varchar("ipv6").bindTo { it.ipv6 }
    var numwant = int("numwant").bindTo { it.numwant }
    var trackerid = varchar("trackerid").bindTo { it.trackerid }
    var authKey = varchar("auth_key").bindTo { it.authKey }

    var createTime = datetime("create_time").bindTo { it.createTime }
    var reportTime = datetime("report_time").bindTo { it.reportTime }
    var status = int("status").bindTo { it.status }
}