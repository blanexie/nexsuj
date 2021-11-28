package com.github.blanexie.nexusj.bencode

import cn.hutool.core.io.FileUtil
import cn.hutool.crypto.digest.DigestUtil
import com.dampcake.bencode.Bencode
import com.github.blanexie.dao.TorrentDO
import com.github.blanexie.nexusj.support.gson
import com.github.blanexie.nexusj.support.setting
import java.nio.ByteBuffer
import java.time.LocalDateTime

val bencode = Bencode(charset("utf8"), true)

fun toTorrent(reqMap: Map<String, Any>): TorrentDO {
    val beMapData = reqMap["torrent"] as MutableMap<String, Any>
    //更新tracker url
    beMapData["announce"] = setting["pt.announce.url"]!!

    val torrent = TorrentDO()
    //开始处理Info中字段
    val infoMap = beMapData["info"] as MutableMap<String, Any>
    //计算infohash值
    val infoBytes = bencode.encode(infoMap)
    torrent.infoHash = DigestUtil.sha1Hex(infoBytes)
    beMapData["info_hash"] = torrent.infoHash
    val torrentPath = setting["torrent.path"]
    torrent.path = "${torrentPath}/${torrent.infoHash}"
    torrent.title = reqMap["title"] as String
    val length = infoMap["length"] as Long?
    torrent.size = if (length == null) {
        val pieces = infoMap["pieces"] as ByteBuffer
        val pieceLength = infoMap["piece length"] as Long
        (pieces.array().size / 20) * pieceLength
    } else {
        length
    }
    torrent.type = reqMap["type"] as String
    torrent.labels = gson.fromJson<List<String>>(reqMap["labels"] as String, List::class.java)
    torrent.coverPath = reqMap["coverPath"] as String
    torrent.description = reqMap["description"] as String

    //开始处理单文件和多文件
    torrent.files = infoMap["files"] as List<Map<String, Any>>?
    torrent.uploadTime = LocalDateTime.now()

    torrent.status = 0
    torrent.ratio = 1

    //保存种子文件
    FileUtil.writeBytes(bencode.encode(beMapData), torrent.path)

    return torrent
}
