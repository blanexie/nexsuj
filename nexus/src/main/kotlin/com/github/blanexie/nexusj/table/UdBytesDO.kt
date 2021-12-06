package com.github.blanexie.nexusj.table

import com.github.blanexie.nexusj.support.database
import org.ktorm.database.Database
import org.ktorm.entity.Entity
import org.ktorm.entity.add
import org.ktorm.entity.sequenceOf
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
