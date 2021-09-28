package com.github.blanexie.dao

import com.github.blanexie.dao.Props.bindTo
import com.github.blanexie.dao.Props.primaryKey
import com.github.blanexie.tracker.server.TrackerReq
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime


/*****************************************************/
val Database.propsDO get() = this.sequenceOf(Props)

interface PropsDO : Entity<PropsDO> {
    companion object : Entity.Factory<UserDO>()

    var id: Int
    var type: String
    var code: String
    var value: String
}

object Props : Table<PropsDO>("props") {
    var id = int("id").primaryKey().bindTo { it.id }
    var type = varchar("type").bindTo { it.type }
    var code = varchar("code").bindTo { it.code }
    var value = varchar("value").bindTo { it.value }
}

/*****************************************************/
val Database.userDO get() = this.sequenceOf(User)

interface UserDO : Entity<UserDO> {
    companion object : Entity.Factory<UserDO>()

    var id: Int
    var email: String
    var pwd: String
    var nick: String
    var sex: Int
    var createTime: LocalDateTime
    var updateTime: LocalDateTime
    var authKey: String
    var status: Int
}

object User : Table<UserDO>("user") {
    var id = int("id").primaryKey().bindTo { it.id }
    var email = varchar("email").bindTo { it.email }
    var pwd = varchar("pwd").bindTo { it.pwd }
    var sex = int("sex").bindTo { it.sex }
    var nick = varchar("nick").bindTo { it.nick }
    var createTime = datetime("create_time").bindTo { it.createTime }
    var updateTime = datetime("update_time").bindTo { it.updateTime }
    var auth_key = varchar("auth_key").bindTo { it.authKey }
    var status = int("status").bindTo { it.status }
}

/*****************************************************/
val Database.userTorrentDO get() = this.sequenceOf(UserTorrent)


interface UserTorrentDO : Entity<UserTorrentDO> {
    companion object : Entity.Factory<UserTorrentDO>()

    var id: Int
    var userId: Int
    var infoHash: String
    var createTime: LocalDateTime
    var authKey: String
    var status: Int

}

object UserTorrent : Table<UserTorrentDO>("user_torrent") {
    var id = int("id").primaryKey().bindTo { it.id }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var userId = int("user_id").bindTo { it.userId }
    var createTime = datetime("create_time").bindTo { it.createTime }
    var authKey = varchar("auth_key").bindTo { it.authKey }
    var status = int("status").bindTo { it.status }
}


/********************************************************/
val Database.peerDO get() = this.sequenceOf(Peer)

interface PeerDO : Entity<PeerDO> {
    companion object : Entity.Factory<PeerDO>() {
        fun build(trackerReq: TrackerReq, userId: Int): PeerDO {
            val peer = PeerDO()
            peer.infoHash = trackerReq.infoHash
            peer.peerId = trackerReq.peerId
            peer.port = trackerReq.port
            peer.uploaded = trackerReq.uploaded
            peer.downloaded = trackerReq.downloaded
            peer.left = trackerReq.left
            peer.compact = trackerReq.compact
            peer.event = trackerReq.event
            peer.ip = trackerReq.ip
            peer.numwant = trackerReq.numwant

            peer.authKey = trackerReq.authKey
            peer.trackerid = trackerReq.trackerId
            peer.createTime = LocalDateTime.now()
            peer.userId = userId
            peer.lastReportTime = LocalDateTime.now()
            return peer
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
    var ip: String
    var numwant: Int
    var trackerid: String?
    var authKey: String

    var userId: Int
    var createTime: LocalDateTime
    var lastReportTime: LocalDateTime

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
    var numwant = int("numwant").bindTo { it.numwant }
    var trackerid = varchar("trackerid").bindTo { it.trackerid }
    var createTime = datetime("create_time").bindTo { it.createTime }

    var userId = int("user_id").bindTo { it.userId }
    var lastReportTime = datetime("last_report_time").bindTo { it.lastReportTime }
    var authKey = varchar("auth_key").bindTo { it.authKey }
}

/***************************************************************/
val Database.torrentDO get() = this.sequenceOf(Torrent)

interface TorrentDO : Entity<TorrentDO> {
    companion object : Entity.Factory<TorrentDO>()

    var id: Int

    var announce: String
    var announceList: List<String>?
    var creationDate: Long?
    var createdBy: String?
    var comment: String?
    var encoding: String?

    //info的信息
    var name: String
    var pieces: String     //所有文件的hash值
    var pieceLength: Long
    var length: Long?
    var private: Int
    var files: List<Map<String, Any>>?

    var status: Int
    var userId: Int
    var uploadTime: LocalDateTime
    var type: String
    var labels: List<String>
    var title: String
    var description: String
}


object Torrent : Table<TorrentDO>("torrent") {
    var id = int("id").primaryKey().bindTo { it.id }

    var announce = varchar("announce").bindTo { it.announce }
    var announceList = json<List<String>>("announce_list").bindTo { it.announceList }
    var createdBy = varchar("created_by").bindTo { it.createdBy }
    var comment = varchar("comment").bindTo { it.comment }
    var creationDate = long("creation_date").bindTo { it.creationDate }
    var encoding = varchar("encoding").bindTo { it.encoding }

    var name = varchar("name").bindTo { it.name }
    var pieces = varchar("pieces").bindTo { it.pieces }
    var pieceLength = long("piece_length").bindTo { it.pieceLength }
    var length = long("length").bindTo { it.length }
    var private = int("private").bindTo { it.private }
    var files = json<List<Map<String, Any>>>("files").bindTo { it.files }

    var userId = int("user_id").bindTo { it.userId }
    var status = int("status").bindTo { it.status }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }

    var type = varchar("type").bindTo { it.type }
    var labels = json<List<String>>("labels").bindTo { it.labels }
    var title = varchar("title").bindTo { it.title }
    var description = varchar("description").bindTo { it.description }

}
