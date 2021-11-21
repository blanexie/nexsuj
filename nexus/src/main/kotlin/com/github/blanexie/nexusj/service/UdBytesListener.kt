package com.github.blanexie.nexusj.service

import com.github.blanexie.dao.PeerDO
import com.github.blanexie.dao.UdBytesDO
import com.github.blanexie.dao.udBytesDO
import com.github.blanexie.nexusj.support.database
import com.github.blanexie.nexusj.support.event.Event
import com.github.blanexie.nexusj.support.event.Listener
import com.github.blanexie.nexusj.support.event.uploadBytes
import org.ktorm.entity.*
import kotlin.reflect.cast


/**
 * 用户积分, 上传量下载量, 定时任务等等操作逻辑
 */
class UdBytesListener : Listener {

    override fun topic(): String {
        return uploadBytes
    }

    override suspend fun process(event: Event<*>) {
        val dataType = event.dataType
        val peerDO = dataType.cast(event.data) as PeerDO
        val udBytesDO = UdBytesDO()
        udBytesDO.infoHash = peerDO.infoHash
        udBytesDO.download = peerDO.downloaded
        udBytesDO.upload = peerDO.uploaded
        udBytesDO.left = peerDO.left
        udBytesDO.status = 0
        udBytesDO.authKey = peerDO.authKey
        udBytesDO.uploadTime = peerDO.reportTime
        udBytesDO.userId= peerDO.userId
        udBytesDO.status=0
        database.udBytesDO.add(udBytesDO)
    }

}




