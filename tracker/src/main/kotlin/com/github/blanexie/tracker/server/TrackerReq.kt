package com.github.blanexie.tracker.server

data class TrackerReq(
    val infoHash: String,
    val peerId: String,
    val port: Int,
    val uploaded: Long,
    val downloaded: Long,
    val left: Long,
    val event: String?=null,
    val ip: String?=null,
    // 此参数值为1,表示期望得到紧凑模式的节点列表.
    //否则表示期望得到普通模式的节点列表.
    //指出客户端是否支持压缩模式. 如果是,伙伴列表将被一个伙伴字符串所代替.每个伙伴占6个字节.前4个字节是主机(网络字序) , 后2个字节是端口(网络字序).
    val compact: Int? = 0,
    val trackerId: String? = null,  // 可选. 如果上次发布含有trackerid,这次就要重新送还.
    val numwant: Int? = 50  //: 可选的期望Tracker最大返回数.缺省为50个.

)
