package com.github.blanexie.nexusj.controller.param

import com.github.blanexie.nexusj.controller.getIpAddress
import io.ktor.request.*
import java.time.LocalDateTime

data class TrackerReq(val request: ApplicationRequest) {
    val infoHash: String = request.uri.substringAfter("info_hash=").substringBefore("&")
    val peerId: String = request.queryParameters["peer_id"]!!
    val port: Int = request.queryParameters["port"]!!.toInt()
    val uploaded: Long = request.queryParameters["uploaded"]!!.toLong()
    val downloaded: Long = request.queryParameters["downloaded"]!!.toLong()
    val left: Long = request.queryParameters["left"]!!.toLong()
    val event: String
    val ip: String

    // 此参数值为1,表示期望得到紧凑模式的节点列表.
    // 否则表示期望得到普通模式的节点列表.
    // 指出客户端是否支持压缩模式. 如果是,伙伴列表将被一个伙伴字符串所代替.每个伙伴占6个字节.前4个字节是主机(网络字序) , 后2个字节是端口(网络字序).
    val compact: Int
    val trackerId: String? = request.queryParameters["trackerid"]?.toString()    // 可选. 如果上次发布含有trackerid,这次就要重新送还.
    val numwant: Int  //: 可选的期望Tracker最大返回数.缺省为50个.
    val authKey: String = request.queryParameters["auth_key"]!!
    var uploadTime: LocalDateTime

    init {
        var numwant = request.queryParameters["numwant"]?.toInt()
        if (numwant == null) {
            this.numwant = 50
        } else {
            this.numwant = numwant
        }
        var event = request.queryParameters["event"]
        if (event == null) {
            this.event = "empty"
        } else {
            this.event = event
        }

        var ip = request.queryParameters["ip"]?.toString()
        if (ip == null) {
            this.ip = getIpAddress(request)!!
        } else {
            this.ip = ip
        }

        var compact = request.queryParameters["compact"]?.toInt()
        if (compact != null) {
            this.compact = compact
        } else {
            this.compact = 1
        }
        this.uploadTime = LocalDateTime.now()
    }

}
