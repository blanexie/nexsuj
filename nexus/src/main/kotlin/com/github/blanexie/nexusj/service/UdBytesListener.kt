package com.github.blanexie.nexusj.service

import com.github.blanexie.dao.UdBytesDO
import com.github.blanexie.dao.database
import com.github.blanexie.dao.udBytesDO
import com.github.blanexie.nexusj.controller.param.TrackerReq
import com.github.blanexie.nexusj.support.event.Event
import com.github.blanexie.nexusj.support.event.Listener
import com.github.blanexie.nexusj.support.event.uploadBytes
import org.ktorm.entity.add
import kotlin.reflect.cast


/**
 * 用户积分, 上传量下载量, 定时任务等等操作逻辑
 */
class UdBytesListener : Listener {

    override fun topic(): String {
        return  uploadBytes
    }

    override suspend fun process(event: Event<*>) {
        val dataType = event.dataType
        val trackerReq = dataType.cast(event.data) as TrackerReq
        val udBytesDO = UdBytesDO()
        udBytesDO.infoHash = trackerReq.infoHash
        udBytesDO.download = trackerReq.downloaded
        udBytesDO.upload = trackerReq.uploaded
        udBytesDO.left = trackerReq.left
        udBytesDO.status = 0
        udBytesDO.authKey = trackerReq.authKey
        udBytesDO.uploadTime = trackerReq.uploadTime

        database.udBytesDO.add(udBytesDO)
    }

}




