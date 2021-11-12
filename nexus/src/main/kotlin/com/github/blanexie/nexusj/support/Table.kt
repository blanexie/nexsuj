package com.github.blanexie.dao


import com.github.blanexie.nexusj.controller.param.TrackerReq
import com.github.blanexie.nexusj.support.json
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.sequenceOf
import org.ktorm.schema.*
import java.time.LocalDateTime


/*********************** 上报流量流水表, 根据这个流水确定用户的总上传 下载 和积分 , 这个表需要定期清理 ******************************/
val Database.udBytesDO get() = this.sequenceOf(UdBytes)

interface UdBytesDO : Entity<UdBytesDO> {
    companion object : Entity.Factory<UdBytesDO>()

    var id: Int

    var authKey: String

    //info部分sha1编码后再进行urlEncode之后的字符串
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

}

object UdBytes : Table<UdBytesDO>("upbytes") {
    var id = int("id").primaryKey().bindTo { it.id }
    var authKey = varchar("auth_key").bindTo { it.authKey }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var upload = long("upload").bindTo { it.upload }
    //下载量
    var download = long("download").bindTo { it.download }
    //剩余量
    var left = long("left").bindTo { it.left }

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

    /**
     * 用户的角色id
     */
    var roleId:Int

    //上传量, 汇总的上传量
    var upload: Long

    //下载量,  汇总的下载量
    var download: Long

    // 积分的大小,  总积分
    var integral: Long

    var createTime: LocalDateTime
    var updateTime: LocalDateTime
    //用户的默认key
    var authKey: String

    /**
     *  0: 正常用户
     * -1: 永久封禁用户
     * -2: 限时封禁用户
     */
    var status: Int

}

object User : Table<UserDO>("user") {
    var id = int("id").primaryKey().bindTo { it.id }
    var email = varchar("email").bindTo { it.email }
    var pwd = varchar("pwd").bindTo { it.pwd }
    var sex = int("sex").bindTo { it.sex }
    var nick = varchar("nick").bindTo { it.nick }

    //上传量, 有效的上传量, 汇总的
    var upload = long("upload").bindTo { it.upload }

    //下载量, 有效的下载量, 汇总的
    var download = long("download").bindTo { it.download }

    // 积分的大小,
    var integral = long("integral").bindTo { it.integral }

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
    //0 : 正常默认状态
    //-1: 当前用户禁止下载这个文件
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
            peer.reportTime = LocalDateTime.now()
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
    var reportTime: LocalDateTime

    //默认0 , 如果-1 就是表示这个peerId被禁用了
    var status: Int

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
    var reportTime = datetime("last_report_time").bindTo { it.reportTime }
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

    //文件总大小
    var size: Long

    //0: 初始化状态 1: 正常可以使用的状态 -1： 非法的种子
    var status: Int

    //种子的上传这id
    var userId: Int

    //上传时间
    var uploadTime: LocalDateTime

    // 种子的类型， 9kg , 电影， 连续剧等
    var type: String

    //种子的标签
    var labels: List<String>

    //标题
    var title: String

    //上传比率， 默认1
    var ratioUp: Float

    //下载比率， 默认1
    var ratioDown: Float

    //上传下载 回复正常的时间点
    var rationTime: LocalDateTime
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


    var ratioUp = float("ratio_up").bindTo { it.ratioUp }
    var ratioDown = float("ratio_down").bindTo { it.ratioDown }
    var rationTime = datetime("ration_time").bindTo { it.rationTime }

}

/*********************************/
val Database.torrentInfoDO get() = this.sequenceOf(Info)

interface InfoDO : Entity<InfoDO> {
    companion object : Entity.Factory<InfoDO>()

    //info部分的sha1值. 默认urlencode编码的字符串
    var infoHash: String

    //十分重要的info信息, 文件的唯一标识
    var info: ByteArray
    var description: String

}

object Info : Table<InfoDO>("info") {
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var info = blob("info").bindTo { it.info }
    var description = text("description").bindTo { it.description }

}
