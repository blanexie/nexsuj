package com.github.blanexie.dao


import com.github.blanexie.tracker.server.TrackerReq
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime


/*********************** 上报流量流水表, 根据这个流水确定用户的总上传 下载 和积分 , 这个表需要定期清理,  ******************************/
val Database.udBytesDO get() = this.sequenceOf(UdBytes)

interface UdBytesDO : Entity<UdBytesDO> {
    companion object : Entity.Factory<UdBytesDO>()

    var id: Int

    var userId: Int

    //urlEncode编码的种子文件
    var infoHash: String

    //上报的时间
    var uploadTime: LocalDateTime

    var upload: Long
    var download: Long
    var left: Long

    //1: 该条记录已经经过认证无误, 并且已经落库用户表中
    //0: 该条记录还未认证
    //-1: 这条上报记录作废
    var status: Int

    /**
     * 本条记录,  有效的计算的上传量
     * 正数加, 负数减
     */
    var changeUpload: Long

    /**
     * 本条记录,  有效的计算的下载量
     *   正数加, 负数减
     */
    var changeDownload: Long

    /**
     * 本条记录,  有效的计算的积分修改量
     *   正数加, 负数减
     */
    var changeIntegral: Long

}

object UdBytes : Table<UdBytesDO>("upbytes") {
    var id = int("id").primaryKey().bindTo { it.id }
    var userId = int("user_id").bindTo { it.userId }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var upload = long("upload").bindTo { it.upload }

    //下载量
    var download = long("download").bindTo { it.download }

    //剩余量
    var left = long("left").bindTo { it.left }

    var changeUpload = long("change_upload").bindTo { it.changeUpload }
    var changeDownload = long("change_download").bindTo { it.changeDownload }
    var changeIntegral = long("change_integral").bindTo { it.changeIntegral }

    var status = int("status").bindTo { it.status }
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

    //上传量, 汇总的上传量
    var upload: Long

    //下载量,  汇总的下载量
    var download: Long

    // 积分的大小,  总积分
    var integral: Long

    var createTime: LocalDateTime
    var updateTime: LocalDateTime
    var authKey: String

    /**
     * 0: 正常用户
     * -1: 封禁用户
     */
    var status: Int

    /**
     * 解封时间, 只有封禁的状态这个字段才有意义
     */
    var unlockTime: LocalDateTime
}

object User : Table<UserDO>("user") {
    var id = int("id").primaryKey().bindTo { it.id }
    var email = varchar("email").bindTo { it.email }
    var pwd = varchar("pwd").bindTo { it.pwd }
    var sex = int("sex").bindTo { it.sex }
    var nick = varchar("nick").bindTo { it.nick }

    //上传量
    var upload = long("upload").bindTo { it.upload }

    //下载量
    var download = long("download").bindTo { it.download }

    // 积分的大小
    var integral = long("integral").bindTo { it.integral }

    var createTime = datetime("create_time").bindTo { it.createTime }
    var updateTime = datetime("update_time").bindTo { it.updateTime }
    var auth_key = varchar("auth_key").bindTo { it.authKey }
    var status = int("status").bindTo { it.status }
    var unlockTime = datetime("unlock_time").bindTo { it.unlockTime }

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
    //默认0 , 如果-1 就是表示这个peerId被禁用了
    var status:Int

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
    var status = int("status").bindTo { it.status }
}

/***************************************************************/
val Database.torrentDO get() = this.sequenceOf(Torrent)

interface TorrentDO : Entity<TorrentDO> {
    companion object : Entity.Factory<TorrentDO>()

    var id: Int

    var announce: String
    var creationDate: Long?
    var createdBy: String?
    var comment: String?
    var encoding: String?

    //info部分的sha1值. 默认urlencode编码的字符串
    var infoHash: String

    //info的信息
    var name: String
    var files: List<Map<String, Any>>?
    var private: Int

    var size: Long  //文件总大小
    var status: Int
    var userId: Int
    var uploadTime: LocalDateTime
    var type: String
    var labels: List<String>
    var title: String

}


object Torrent : Table<TorrentDO>("torrent") {
    var id = int("id").primaryKey().bindTo { it.id }

    var announce = varchar("announce").bindTo { it.announce }
    var createdBy = varchar("created_by").bindTo { it.createdBy }
    var comment = varchar("comment").bindTo { it.comment }
    var creationDate = long("creation_date").bindTo { it.creationDate }
    var encoding = varchar("encoding").bindTo { it.encoding }

    var infoHash = varchar("info_hash").bindTo { it.infoHash }

    var name = varchar("name").bindTo { it.name }
    var private = int("private").bindTo { it.private }
    var files = json<List<Map<String, Any>>>("files").bindTo { it.files }


    var size = long("size").bindTo { it.size }
    var userId = int("user_id").bindTo { it.userId }
    var status = int("status").bindTo { it.status }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var type = varchar("type").bindTo { it.type }
    var labels = json<List<String>>("labels").bindTo { it.labels }
    var title = varchar("title").bindTo { it.title }

}

/*********************************/
val Database.torrentInfoDO get() = this.sequenceOf(TorrentInfo)

interface TorrentInfoDO : Entity<TorrentInfoDO> {
    companion object : Entity.Factory<TorrentInfoDO>()

    //info部分的sha1值. 默认urlencode编码的字符串
    var infoHash: String

    //十分重要的info信息, 文件的唯一标识
    var info: ByteArray
    var description: String

}

object TorrentInfo : Table<TorrentInfoDO>("torrent_info") {
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var info = blob("info").bindTo { it.info }
    var description = varchar("description").bindTo { it.description }

}
