package com.github.blanexie.dao


import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.dsl.and
import org.ktorm.dsl.eq
import org.ktorm.dsl.greaterEq
import org.ktorm.entity.*
import org.ktorm.jackson.json
import org.ktorm.schema.*
import java.time.LocalDateTime


/*********************** 上报流量流水表, 根据这个流水确定用户的总上传 下载 和积分 , 这个表需要定期清理 ******************************/
val Database.udBytesDO get() = this.sequenceOf(UdBytes)

interface UdBytesDO : Entity<UdBytesDO> {
    companion object : Entity.Factory<UdBytesDO>() {


    }

    var id: Int
    var authKey: String
    var infoHash: String   //info部分sha1编码后再进行urlEncode之后的字符串
    var uploadTime: LocalDateTime   //上报的时间
    var userId: Int
    var upload: Long
    var download: Long
    var left: Long

    //1: 该条记录已经经过认证无误, 并且已经落库用户表中
    //0: 该条记录还未认证
    //-1: 这条上报记录作废
    var status: Int


    fun save() {
        database().udBytesDO.add(this)
    }

}

object UdBytes : Table<UdBytesDO>("upbytes") {
    var id = int("id").primaryKey().bindTo { it.id }
    var authKey = varchar("auth_key").bindTo { it.authKey }
    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var userId = int("user_id").bindTo { it.userId }
    var upload = long("upload").bindTo { it.upload }
    var download = long("download").bindTo { it.download }     //下载量
    var left = long("left").bindTo { it.left }    //剩余量
    var status = int("status").bindTo { it.status }
}

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

/*****************************************************/
val Database.userDO get() = this.sequenceOf(User)

interface UserDO : Entity<UserDO> {
    companion object : Entity.Factory<UserDO>()

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


/********************************************************/
val Database.peerDO get() = this.sequenceOf(Peer)

interface PeerDO : Entity<PeerDO> {
    companion object : Entity.Factory<PeerDO>()

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

/***************************************************************/
val Database.torrentDO get() = this.sequenceOf(Torrent)

interface TorrentDO : Entity<TorrentDO> {
    companion object : Entity.Factory<TorrentDO>() {
        fun findById(id: Int): TorrentDO? {
            return database().torrentDO.singleOrNull { it.id eq id }
        }

        fun findByInfoHash(infoHash: String): TorrentDO? {
            return database().torrentDO.singleOrNull { it.infoHash eq infoHash }
        }
    }

    var id: Int

    //info部分的sha1值. 默认urlencode编码的字符串
    var infoHash: String

    //torrent文件保存服务器的相对地址
    var path: String

    //标题
    var title: String

    //文件总大小
    var size: Long

    // 种子的类型， 9kg , 电影， 连续剧等
    var type: String

    //种子的标签
    var labels: List<String>

    //封面图片
    var coverPath: String

    //文件描述
    var description: String

    //种子中文件列表
    var files: List<Map<String, Any>>?

    //上传时间
    var uploadTime: LocalDateTime

    //0: 初始化状态 1: 正常可以使用的状态 -1： 非法的种子
    var status: Int

    //种子 优惠力度
    //1: 正常上传下载 2： 免费下载， 正常上传 3：免费下载，双倍上传 ........
    var ratio: Int

    //上传下载 回复正常的时间点
    var rationTime: LocalDateTime

    fun save() {
        database().torrentDO.add(this)
    }
}


object Torrent : Table<TorrentDO>("torrent") {
    var id = int("id").primaryKey().bindTo { it.id }

    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var path = varchar("path").bindTo { it.path }

    var title = varchar("title").bindTo { it.title }
    var size = long("size").bindTo { it.size }
    var type = varchar("type").bindTo { it.type }
    var labels = json<List<String>>("labels").bindTo { it.labels }

    //封面图片
    var coverPath = varchar("cover_path").bindTo { it.coverPath }

    //文件描述
    var description = text("description").bindTo { it.description }
    var files = json<List<Map<String, Any>>>("files").bindTo { it.files }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var status = int("status").bindTo { it.status }
    var ratio = int("ratio").bindTo { it.ratio }
    var rationTime = datetime("ration_time").bindTo { it.rationTime }

}
