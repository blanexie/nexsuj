package com.github.blanexie.nexusj.table


import cn.hutool.core.date.LocalDateTimeUtil
import com.github.blanexie.nexusj.controller.param.TorrentQuery
import com.github.blanexie.nexusj.support.database
import com.github.blanexie.nexusj.support.gson
import org.ktorm.database.Database
import org.ktorm.database.asIterable
import org.ktorm.dsl.eq
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
import org.ktorm.entity.singleOrNull
import org.ktorm.jackson.json
import org.ktorm.schema.*
import org.slf4j.LoggerFactory
import java.sql.Connection
import java.time.LocalDateTime

/***************************************************************/

val logger = LoggerFactory.getLogger("TorrentDO")
val Database.torrentDO get() = this.sequenceOf(Torrent)

interface TorrentDO : Entity<TorrentDO> {
    companion object : Entity.Factory<TorrentDO>() {
        fun findById(id: Int): TorrentDO? {
            return database().torrentDO.singleOrNull { it.id eq id }
        }

        fun findByInfoHash(infoHash: String): TorrentDO? {
            return database().torrentDO.singleOrNull { it.infoHash eq infoHash }
        }

        fun findByQuery(torrentQuery: TorrentQuery, currentUserId: Int): List<TorrentDO> {
            val torrentList = database().useConnection { conn ->
                val (params, sqlB) = buildSqlAndParams(torrentQuery, currentUserId)
                logger.info("sql:{}", sqlB)
                listTorrent(conn, sqlB, params)
            }
            return torrentList
        }
    }

    var id: Int

    //info部分的sha1值. 默认urlencode编码的字符串
    var infoHash: String

    //种子中的文件名
    var name: String

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

    //关联图片
    var imgList: List<String>

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
    var ration: Int

    //上传下载 回复正常的时间点
    var rationTime: LocalDateTime?

    //上传的用户
    var userId: Int


    var left: Long?
    var downloaded: Long?
    var event: String?
    var uploaded: Long?
    var utStatus: Int?

    fun save() {
        database().torrentDO.add(this)
    }
}


object Torrent : Table<TorrentDO>("torrent") {
    var id = int("id").primaryKey().bindTo { it.id }

    var infoHash = varchar("info_hash").bindTo { it.infoHash }
    var name = varchar("name").bindTo { it.name }
    var title = varchar("title").bindTo { it.title }
    var size = long("size").bindTo { it.size }
    var type = varchar("type").bindTo { it.type }
    var labels = json<List<String>>("labels").bindTo { it.labels }

    //封面图片
    var coverPath = varchar("cover_path").bindTo { it.coverPath }
    var imgList = json<List<String>>("img_list").bindTo { it.imgList }

    //文件描述
    var description = text("description").bindTo { it.description }
    var files = json<List<Map<String, Any>>>("files").bindTo { it.files }
    var uploadTime = datetime("upload_time").bindTo { it.uploadTime }
    var status = int("status").bindTo { it.status }
    var ration = int("ration").bindTo { it.ration }
    var rationTime = datetime("ration_time").bindTo { it.rationTime }
    var userId = int("user_id").bindTo { it.userId }

}


private fun listTorrent(
    conn: Connection,
    sqlB: StringBuilder,
    params: MutableList<Any>
): List<TorrentDO> {
    val torrents = conn.prepareStatement(sqlB.toString()).use { statement ->
        for ((index, param) in params.withIndex()) {
            statement.setObject(index + 1, param)
        }
        val torrentList = statement.executeQuery().asIterable().map {
            val torrentDO = TorrentDO()
            torrentDO.id = it.getInt("id")
            torrentDO.description = it.getString("description")
            torrentDO.ration = it.getInt("ration")
            torrentDO.coverPath = it.getString("cover_path")
            torrentDO.imgList = gson.fromJson<List<String>>(it.getString("img_list"), List::class.java)
            torrentDO.files = gson.fromJson<List<Map<String, Any>>>(it.getString("files"), List::class.java)
            torrentDO.infoHash = it.getString("info_hash")
            torrentDO.labels = gson.fromJson<List<String>>(it.getString("labels"), List::class.java)
            torrentDO.rationTime = LocalDateTimeUtil.of(it.getTimestamp("ration_time"))
            torrentDO.size = it.getLong("size")
            torrentDO.status = it.getInt("status")
            torrentDO.name = it.getString("name")
            torrentDO.title = it.getString("title")
            torrentDO.type = it.getString("type")
            torrentDO.userId = it.getInt("user_id")
            torrentDO.uploadTime = LocalDateTimeUtil.of(it.getTimestamp("upload_time"))

            torrentDO.downloaded = it.getLong("downloaded")
            torrentDO.left = it.getLong("left")
            torrentDO.uploaded = it.getLong("uploaded")
            torrentDO.event = it.getString("event")
            val utStatus = it.getString("ut_status")
            logger.info("直接取出： $utStatus")
            if (utStatus != null) {
                torrentDO.utStatus = utStatus.toInt()
            }
            logger.info("测试： ${torrentDO.utStatus}")
            torrentDO
        }
        torrentList
    }
    return torrents
}

private fun buildSqlAndParams(torrentQuery: TorrentQuery, currentUserId: Int): Pair<MutableList<Any>, StringBuilder> {
    val params = mutableListOf<Any>()

    val sqlB = StringBuilder(
        """
            select t.*, ut.status as ut_status, p.`left`, p.downloaded, p.event, p.uploaded  from torrent t
            left join  user_torrent ut on ut.info_hash = t.info_hash and ut.user_id = ${currentUserId}
            left join  peer p on t.info_hash= p.info_hash and ut.auth_key = p.auth_key
            where 1 = 1 
        """.trimIndent()
    )
    if (torrentQuery.infoHash != null) {
        sqlB.append(" and info_hash = ? ")
        params.add(torrentQuery.infoHash!!)
    }
    if (torrentQuery.title != null) {
        sqlB.append(" and title = ? ")
        params.add(torrentQuery.title!!)
    }
    if (torrentQuery.type != null) {
        sqlB.append(" and type = ? ")
        params.add(torrentQuery.type!!)
    }
    if (torrentQuery.ratio != null) {
        sqlB.append(" and ratio = ? ")
        params.add(torrentQuery.ratio!!)
    }
    if (torrentQuery.labels != null && torrentQuery.labels!!.isNotEmpty()) {
        sqlB.append(" and (")
        var first = true
        torrentQuery.labels!!.forEach {
            if (first) {
                sqlB.append(" JSON_CONTAINS( '[1,2,3,4]', ?, '$')  ")
                params.add(it!!)
            } else {
                sqlB.append(" OR JSON_CONTAINS( '[1,2,3,4]',  ?, '$')  ")
                params.add(it!!)
            }
        }
        sqlB.append(" ) ")
    }
    sqlB.append(" limit ?,?")
    params.add((torrentQuery.pageNo - 1) * torrentQuery.pageSize)
    params.add(torrentQuery.pageSize)

    return Pair(params, sqlB)
}
