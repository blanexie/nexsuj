package com.github.blanexie.tracker.server

import io.ktor.application.*
import io.ktor.request.*

data class TrackerReq(val request: ApplicationRequest) {
    val infoHash: String
    val peerId: String
    val port: Int
    val uploaded: Long
    val downloaded: Long
    val left: Long
    val event: String
    val ip: String

    // 此参数值为1,表示期望得到紧凑模式的节点列表.
    //否则表示期望得到普通模式的节点列表.
    //指出客户端是否支持压缩模式. 如果是,伙伴列表将被一个伙伴字符串所代替.每个伙伴占6个字节.前4个字节是主机(网络字序) , 后2个字节是端口(网络字序).
    val compact: Int
    val trackerId: String?    // 可选. 如果上次发布含有trackerid,这次就要重新送还.
    val numwant: Int  //: 可选的期望Tracker最大返回数.缺省为50个.
    val authKey: String

    init {
        infoHash = request.uri.substringAfter("info_hash=").substringBefore("&")
        authKey = request.queryParameters["authKey"]!!
        peerId = request.queryParameters["peer_id"]!!
        port = request.queryParameters["port"]!!.toInt()
        uploaded = request.queryParameters["uploaded"]!!.toLong()
        downloaded = request.queryParameters["downloaded"]!!.toLong()
        left = request.queryParameters["left"]!!.toLong()
        trackerId = request.queryParameters["trackerid"]?.toString()
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

    }


}
