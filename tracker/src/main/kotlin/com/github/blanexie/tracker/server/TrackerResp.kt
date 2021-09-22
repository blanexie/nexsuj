package com.github.blanexie.tracker.server

data class TrackerResp(
    val interval: Int, // 告知 downloader 隔一段时间后再来查询； 单位为秒
    val peers: List<Peer>, // 返回一个 peer 列表，其中每一项都由 id, ip, port 组成；

    //通常还会有如下一些字段出现:
    val donePeers: Int,  // 下载完毕的节点个数

    val failureReason: String?, // 如果有本项,说明发生了一个严重错误,将不会返回其他任何信息. 键值是人类可读的错误信息.
    val warningMessage: String?, // (新的) 键值是人类可读的的一般警告信息.
    val minInterval: Int, // 最小的发布间隔时间 (秒). 限制客户端重新发布.
    val trackerId: String, //  一个必须被回送的字符串,当客户端再次发布.
    val complete: Int, //  整数, 拥有完全文件的伙伴数.
    val incomplete: Int, // 整数, 拥有不完全文件的伙伴数,也就是"水蛭".
    val key: String, // (可选)，不和其他用户共享的附加标识。当客户端IP地址改变时，可以使用该标识来证明自己的身份。
)

data class Peer(val ip: String, val port: Int, val peerId: String)

