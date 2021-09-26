package com.github.blanexie.dao

import com.github.blanexie.dao.Torrent.bindTo
import com.github.blanexie.tracker.server.TrackerReq
import io.ktor.routing.*
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime


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


/*****************************************************/
val Database.torrentInfoDO get() = this.sequenceOf(TorrentInfo)

interface TorrentInfoDO : Entity<TorrentInfoDO> {
    companion object : Entity.Factory<TorrentInfoDO>()

    var id: Int
    var infoHash: String
    var name: String
    var pieces: Long
    var length: Long
    var private: Int
    var files: String

}

object TorrentInfo : Table<TorrentInfoDO>("torrent_info") {
    var id = int("id").primaryKey().bindTo { it.id }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var name = varchar("name").bindTo { it.name }
    var pieces = long("pieces").bindTo { it.pieces }
    var length = long("length").bindTo { it.length }
    var private = int("private").bindTo { it.private }
    var files = text("files").bindTo { it.files }
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
    var createTime: LocalDateTime
    var userId: Int
    var lastReportTime: LocalDateTime
    var authKey: String
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
    var infoHash: String
    var annnounce: String
    var createDate: LocalDateTime
    var uploadTime: LocalDateTime

    var createBy: String?
    var comment: String?
    var encoding: String?

    var status: Int
    var userId: Int

}


object Torrent : Table<TorrentDO>("torrent") {
    var id = int("id").primaryKey().bindTo { it.id }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var annnounce = varchar("annnounce").bindTo { it.annnounce }
    var createBy = varchar("create_by").bindTo { it.createBy }
    var comment = varchar("comment").bindTo { it.comment }
    var createDate = datetime("create_date").bindTo { it.createDate }
    var encoding = varchar("encoding").bindTo { it.encoding }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var userId = int("user_id").bindTo { it.userId }
    var status = int("status").bindTo { it.status }
}
